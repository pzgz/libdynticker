/**
 *
 */
package mobi.boilr.libdynticker.houbi;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

/**
 * @author andre
 *
 */
public class HuobiExchange extends Exchange {

	/**
	 * @param experiedPeriod
	 */
	public HuobiExchange(long experiedPeriod) {
		super("Huobi", experiedPeriod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mobi.boilr.libdynticker.core.Exchange#getPairsFromAPI()
	 */
	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "CNY"));
		pairs.add(new Pair("LTC", "CNY"));
		return pairs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mobi.boilr.libdynticker.core.Exchange#getTicker(mobi.boilr.
	 * libdynticker.core.Pair)
	 */
	@Override
	protected String getTicker(Pair pair) throws IOException {
		String url = "http://market.huobi.com/staticmarket/ticker_" + pair.getCoin().toLowerCase() + "_json.js";
		return this.parseJSON((new ObjectMapper()).readTree(new URL(url)), pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("ticker").get("last").getTextValue();
	}

}
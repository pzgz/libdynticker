/**
 * /**
 *
 */
package mobi.boilr.libdynticker.okcoin;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

/**
 * @author andre
 *
 */
public class OKCoinExchange extends Exchange {

	/**
	 * @param experiedPeriod
	 */
	public OKCoinExchange(long experiedPeriod) {
		super("OKCoin", experiedPeriod);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see mobi.boilr.libdynticker.core.Exchange#getPairsFromAPI()
	 */
	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new LinkedList<Pair>();
		pairs.add(new Pair("BTC", "USD"));
		pairs.add(new Pair("LTC", "USD"));
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
		String url = "";
		if (pair.getExchange().equals("USD")) {
			// https://www.okcoin.com/api/ticker.do?symbol=ltc_usd&ok=1
			url = "https://www.okcoin.com/api/ticker.do?symbol=" + pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase() + "&ok=1";
		} else if (pair.getExchange().equals("CNY")) {
			url = "https://www.okcoin.cn/api/ticker.do?symbol=" + pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase();
		} else {

		}

		JsonNode node = (new ObjectMapper()).readTree(new URL(url));
		return parseJSON(node, pair);
	}

	@Override
	public String parseJSON(JsonNode node, Pair pair) {
		return node.get("ticker").get("last").getTextValue();
	}
}

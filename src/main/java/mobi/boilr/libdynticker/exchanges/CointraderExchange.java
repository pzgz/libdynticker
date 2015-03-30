package mobi.boilr.libdynticker.exchanges;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.boilr.libdynticker.core.Exchange;
import mobi.boilr.libdynticker.core.Pair;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public final class CointraderExchange extends Exchange {
	private static final List<Pair> PAIRS;
	
	static {
		List<Pair> pairs = new ArrayList<Pair>();
		pairs.add(new Pair("BTC", "USD"));
		pairs.add(new Pair("BTC", "CAD"));
		PAIRS = Collections.unmodifiableList(pairs);
	}
	
	public CointraderExchange(long expiredPeriod) {
		super("Cointradder", expiredPeriod);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		return PAIRS;
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		JsonNode node = readJsonFromUrl("https://www.cointrader.net/api4/stats/daily/" +
				pair.getCoin() + pair.getExchange());
		if(node.get("success").asText().equals("false")) {
			throw new IOException(node.get("message").asText());
		}else{
			return parseTicker(node, pair);
		}
	}

	@Override
	public String parseTicker(JsonNode node, Pair pair) throws IOException {
		return node.get("data").get(pair.getCoin().toUpperCase() + pair.getExchange().toUpperCase())
					.get("lastTradePrice").asText();
	}
}
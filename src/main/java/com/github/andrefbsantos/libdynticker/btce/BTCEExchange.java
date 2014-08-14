package com.github.andrefbsantos.libdynticker.btce;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

/**
 *
 *
 * @author andre
 *
 */
public class BTCEExchange extends Exchange {

	public BTCEExchange(long experiedPeriod) {
		super("BTC-E", experiedPeriod);
	}

	protected String getTickerURL(Pair pair) {
		return "https://btc-e.com/api/3/ticker/" + pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.andrefbsantos.libdynticker.core.Exchange#parseJSON(org.codehaus.jackson.JsonNode,
	 * com.github.andrefbsantos.libdynticker.core.Pair)
	 */
	@Override
	public String parseJSON(JsonNode node, Pair pair) throws IOException {
		if (node.has(pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase())) {
			return node.get(pair.getCoin().toLowerCase() + "_" + pair.getExchange().toLowerCase()).get("last").toString();
		} else {
			throw new IOException(node.get("error").getTextValue());
		}
	}

	@Override
	protected String getTicker(Pair pair) throws IOException {
		return parseJSON(new ObjectMapper().readTree(new URL(this.getTickerURL(pair))), pair);
	}

	@Override
	protected List<Pair> getPairsFromAPI() throws IOException {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<String> elements = (new ObjectMapper()).readTree(new URL("https://btc-e.com/api/3/info")).get("pairs").getFieldNames();
		while (elements.hasNext()) {
			String element = elements.next();
			String[] split = element.split("_");
			String coin = split[0].toUpperCase();
			String exchange = split[1].toUpperCase();
			pairs.add(new Pair(coin, exchange));
		}
		return pairs;
	}

}
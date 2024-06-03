package com.rmendes.route;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rmendes.model.FastFood;

import io.vertx.core.json.JsonObject;

public class Kafka2BQRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("kafka:{{kafka.topic.name}}?brokers={{kafka.external.bootstrap.url}}"
				+ "&sslTruststoreLocation={{kafka.security.truststore.path}}"
				+ "&sslTruststorePassword={{kafka.security.truststore.password}}"
				+ "&securityProtocol={{kafka.security.protocol}}"
				+ "&autoOffsetReset=earliest")
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				JsonObject dbzJson = new JsonObject((String) exchange.getIn().getBody());
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				FastFood fastFood = mapper.readValue(dbzJson.getJsonObject("payload").getJsonObject("after").encodePrettily(), FastFood.class);
				Gson gson = new Gson();
				exchange.getIn().setBody(gson.fromJson(mapper.writeValueAsString(fastFood), new TypeToken<Map<String, Object>>() {}.getType()));
			}
		})
		.to("google-bigquery://bq-teste-424014:demo_dataset:fast_food");

	}

}

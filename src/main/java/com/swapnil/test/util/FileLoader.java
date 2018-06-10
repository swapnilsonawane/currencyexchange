package com.swapnil.test.util;

import com.swapnil.test.dto.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileLoader {

    @Autowired
    private ResourceLoader resourceLoader;

    private static final String pattern = "1 #cur traded at #rate";

    public List<ExchangeRate> loadFile(String date) {
        List<ExchangeRate> exchangeRates = new ArrayList<ExchangeRate>();
        try {
            Resource res = resourceLoader.getResource("classpath:/rates/"+date+".txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(res.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                ExchangeRate rate = new ExchangeRate();
                line = line.replace(" traded at ",",");
                line = line.replace(" times USD","");
                String[] rates = line.split(",");
                if(null!=rates && rates.length==2 ){
                    String cur = rates[0];
                    String rateInStr = rates[1];
                    rate.setCurrency(cur.replace("1 ",""));
                    rate.setRate(new BigDecimal(rateInStr));
                    rate.setBaseCurrency("USD");
                    rate.setDate(date);
                    exchangeRates.add(rate);
                }
                System.out.println(line);

            }
        } catch (Exception e){

        }
        return exchangeRates;

    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}

package com.swapnil.test.callable;

import com.swapnil.test.dto.ExchangeRate;
import com.swapnil.test.util.FileLoader;

import java.util.List;
import java.util.concurrent.Callable;

public class LoaderCallable implements Callable<ExchangeRate> {


    private FileLoader fileLoader;

    private String date;

    private String currency;

    public LoaderCallable(FileLoader fileLoader,String date,String currency){
        this.fileLoader = fileLoader;
        this.date = date;
        this.currency =currency;

    }


    public ExchangeRate call() throws Exception {
        List<ExchangeRate> list =  fileLoader.loadFile(date);
        ExchangeRate exchangeRate = null;
        if(null!=list){
            for(ExchangeRate rate : list) {
                if(currency.equals(rate.getCurrency())){
                    exchangeRate = rate;
                }
            }
        }
        return exchangeRate;
    }
}

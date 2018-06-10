package com.swapnil.test.service.impl;


import com.swapnil.test.callable.LoaderCallable;
import com.swapnil.test.dto.ExchangeRate;
import com.swapnil.test.exception.DataException;
import com.swapnil.test.service.ExchangeService;
import com.swapnil.test.util.FileLoader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class ExchangeRateServiceImpl implements ExchangeService {

    @Autowired
    private FileLoader fileLoader;

    public List<ExchangeRate> getExchangeRateOfDay(String date) throws Exception{
        if(StringUtils.isBlank(date))
            throw new DataException("date parameter may be null");
        List<ExchangeRate> list = fileLoader.loadFile(date);
        return list;

    }

    public ExchangeRate findRate(String date, String baseCurrency, String currency) throws Exception {

        if(StringUtils.isBlank(baseCurrency) || StringUtils.isBlank( currency))
            throw new DataException("check all request parameters on of them may be null");

        baseCurrency  = baseCurrency.toUpperCase();
        currency = currency.toUpperCase();
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrency(baseCurrency);
        exchangeRate.setCurrency(currency);

        List<ExchangeRate> list = fileLoader.loadFile(date);
        ExchangeRate baseCur = new ExchangeRate();
        baseCur.setCurrency(baseCurrency);

        ExchangeRate secondCur = new ExchangeRate();
        secondCur.setCurrency(currency);
        if(null!=list){
            for(ExchangeRate rate : list){
                if(baseCur.getCurrency().equals(rate.getCurrency())){
                    baseCur.setRate(rate.getRate());
                }
                if(secondCur.getCurrency().equals(rate.getCurrency())){
                    secondCur.setRate(rate.getRate());
                }
            }
        }
        //BigDecimal rate = secondCur.getRate().divide(baseCur.getRate(),2);
        BigDecimal rate = baseCur.getRate().divide(secondCur.getRate(),2);
        exchangeRate.setRate(rate);
        exchangeRate.setDate(date);
        return exchangeRate;
    }

    public List<ExchangeRate> findAll(String startDate, String endDate, String currency) throws Exception{
        if(StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate) || StringUtils.isBlank(currency))
            throw new DataException("check all request parameters on of them may be null");
        List<ExchangeRate> finalList = new ArrayList<ExchangeRate>();
        try {
            List<String> dateList =getDateRange(startDate,endDate);
            if(null!=dateList){
                List<Future<ExchangeRate>> futureobjs = new ArrayList<Future<ExchangeRate>>();
                int threadCount = 4;
                if(dateList.size()<4){
                    threadCount = dateList.size();
                }
                ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
                for(String date : dateList){
                    LoaderCallable loaderCallable = new LoaderCallable(fileLoader,date,currency);
                    Future<ExchangeRate> task = executorService.submit(loaderCallable);
                    futureobjs.add(task);
                }
                for(Future<ExchangeRate> obj : futureobjs){
                    ExchangeRate rate= obj.get();
                    if(null!=rate)
                        finalList.add(rate);
                }
                executorService.shutdown();
            }
            return finalList;
        } catch (ParseException e) {
            throw new Exception("Error in parsing ",e);
        } catch (Exception e){
            throw new Exception("Internal Server Error");
        }
    }


    private List<String> getDateRange(String startDate,String endDate) throws ParseException {
        List<String> list = new ArrayList<String>();
        list.add(startDate);
        SimpleDateFormat parser=new SimpleDateFormat("yyyy-MM-dd");
        Date toDate = parser.parse(endDate);
        Date fromDate = parser.parse(startDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        while (cal.getTime().before(toDate)) {
            cal.add(Calendar.DATE, 1);
            list.add(parser.format(cal.getTime()));
        }
        return list;
    }

    public void setFileLoader(FileLoader fileLoader) {
        this.fileLoader = fileLoader;
    }
}

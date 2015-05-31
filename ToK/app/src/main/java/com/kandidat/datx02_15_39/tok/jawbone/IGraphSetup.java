package com.kandidat.datx02_15_39.tok.jawbone;

import com.jjoe64.graphview.series.DataPoint;

import java.util.Date;
import java.util.List;

/**
 * Interface used in combination with the graph view graph container to help set up and use it
 */
public interface IGraphSetup {

    /**
     * Method used to fetch the data points from the diary.
     * @param date used to define what date is used when fetching the data points
     * @return a list containing all DataPoint series that is to be used in the graph
     */
    public List<DataPoint[]> fetchDataPoints(Date date);

    /**
     * Method used to set up the graph used with the initial values
     */
    public void setupGraph();
}

package com.kandidat.datx02_15_39.tok.model.weight;

/**
 * Class used to represent a weight of a specific time
 */
public class Weight {

    private double weight;

    /**
     * Constructor adding a weight
     * @param weight is the weight to be added and used
     */
    public Weight(double weight) {
        this.weight = weight;
    }

    /**
     * Get method for fetching the weight
     * @return the weight represented by a double
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Set method used for setting a new weight
     * @param weight is the new weight to be set
     */
	public void setWeight(double weight){this.weight = weight;}


}

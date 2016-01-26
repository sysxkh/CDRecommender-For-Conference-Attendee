/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resultInfo;

/**
 *
 * @author Kehao Xu
 */
public class SScore 
{
    private double gsCS;

    public double getGsCS() {
        return gsCS;
    }

    public void setGsCS(double gsCS) {
        this.gsCS = gsCS;
    }

    public double getRgCS() {
        return rgCS;
    }

    public void setRgCS(double rgCS) {
        this.rgCS = rgCS;
    }

    public double getWkCS() {
        return wkCS;
    }

    public void setWkCS(double wkCS) {
        this.wkCS = wkCS;
    }

    public double getTtCS() {
        return ttCS;
    }

    public void setTtCS(double ttCS) {
        this.ttCS = ttCS;
    }

    public double getNmCS() {
        return nmCS;
    }

    public void setNmCS(double nmCS) {
        this.nmCS = nmCS;
    }

    public double getGsJC() {
        return gsJC;
    }

    public void setGsJC(double gsJC) {
        this.gsJC = gsJC;
    }

    public double getRgJC() {
        return rgJC;
    }

    public void setRgJC(double rgJC) {
        this.rgJC = rgJC;
    }

    public double getTtJC() {
        return ttJC;
    }

    public void setTtJC(double ttJC) {
        this.ttJC = ttJC;
    }

    public double getTotalCS() {
        return totalCS;
    }

    public void setTotalCS(double totalCS) {
        this.totalCS = totalCS;
    }

    public double getTotalJC() {
        return totalJC;
    }

    public void setTotalJC(double totalJC) {
        this.totalJC = totalJC;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double Total) {
        this.total = Total;
    }
    private double rgCS;
    private double wkCS;
    private double ttCS;
    private double nmCS;
    private double gsJC;
    private double rgJC;
    private double ttJC;
    private double totalCS;
    private double totalJC;
    private double total;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public SScore(double gsCS, double rgCS , double wkCS , double ttCS , double nmCS , double gsJC , double rgJC , double ttJC )
    {
        this.rgCS = rgCS;
        this.wkCS = wkCS;
        this.ttCS = ttCS;
        this.nmCS = nmCS;
        this.gsCS = gsCS;
        this.gsJC = gsJC;
        this.rgJC = rgJC;
        this.ttJC = ttJC;
        this.totalCS = (double) (this.rgCS+this.wkCS+this.ttCS+this.nmCS+this.gsCS)/ (double) 5.0;
        this.totalJC =(double) (this.gsJC+this.rgJC+this.ttJC)/(double) 3.0;
        this.total = (double) (this.totalCS+this.totalJC)/(double) 2.0;
        
    }
    
}

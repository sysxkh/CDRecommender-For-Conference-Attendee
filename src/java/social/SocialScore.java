/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package social;

import application.JaccardCoefficient;
import application.Similarity;
import org.json.JSONObject;
import resultInfo.SScore;
import textHandler.StopwordsRemover;

/**
 *
 * @author Kehao Xu
 */
public class SocialScore 
{
    private SocialInfo si1;
    
    public SocialInfo getSi1() {
        return si1;
    }

    public void setSi1(SocialInfo si1) {
        this.si1 = si1;
    }

    public SocialInfo getSi2() {
        return si2;
    }

    public void setSi2(SocialInfo si2) {
        this.si2 = si2;
    }
    private SocialInfo si2;
    StopwordsRemover swr;
    private String name;

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public SocialScore(SocialInfo si1,SocialInfo si2,StopwordsRemover swr)
    {
        this.si1 = si1;
        this.si2 = si2;
        this.swr = swr;
    }
    
    public SScore calcScore()
    {
        if(si1.getInfomation().isEmpty())
            si1.getInfo();
        if(si2.getInfomation().isEmpty())
            si2.getInfo();
        double gsCS = Similarity.calcSimilarity(si1.getGsp().getInfo(), si2.getGsp().getInfo(), this.swr);
        double rgCS = Similarity.calcSimilarity(si1.getRgp().getInfo(), si2.getRgp().getInfo(), this.swr);
        double wkCS = Similarity.calcSimilarity(si1.getWp().getInfo(), si2.getWp().getInfo(), this.swr);
        double ttCS = Similarity.calcSimilarity(si1.getTp().getInfo(), si2.getTp().getInfo(),this.swr);
        double nmCS = Similarity.calcSimilarity(si1.getInfomation(),si2.getInfomation() , this.swr);
        double gsJC = JaccardCoefficient.calcJaccard(si1.getGsp().getNeighbor(), si2.getGsp().getNeighbor());
        double rgJC = JaccardCoefficient.calcJaccard(si1.getRgp().getNeighbor(), si2.getRgp().getNeighbor());
        double ttJC = JaccardCoefficient.calcJaccard(si1.getTp().getNeighbor(), si2.getTp().getNeighbor());
        SScore ss = new SScore(gsCS,rgCS,wkCS,ttCS,nmCS,gsJC,rgJC,ttJC);      
        ss.setName(this.name);
        return ss;
    }
    
    
    
}

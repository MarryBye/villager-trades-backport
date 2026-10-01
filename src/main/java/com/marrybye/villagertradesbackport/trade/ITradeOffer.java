package com.marrybye.villagertradesbackport.trade;

public interface ITradeOffer {

    int getVillagerXp();

    void setVillagerXp(int xp);

    float getPriceMultiplier();

    void setPriceMultiplier(float priceMultiplier);

    int getDemand();

    void setDemand(int demand);
}

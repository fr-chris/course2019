package com.agentecon.exercise2;

import com.agentecon.agent.Endowment;
import com.agentecon.agent.IAgentIdGenerator;
import com.agentecon.configuration.BasicEconomyConfiguration;
import com.agentecon.consumer.IUtility;
import com.agentecon.consumer.MortalConsumer;
import com.agentecon.goods.Inventory;
import com.agentecon.market.IPriceTakerMarket;
import com.agentecon.util.MovingAverage;

public class GeneralConsumer extends MortalConsumer {
	
	private static final double POTATOE_RESERVE = 0.0;
	private static final double MONEY_RESERVE = 0.2;
	
	private MovingAverage dailySpendings;

	/**
	 * Constructs a new consumer.
	 * Note that internally, it is known in advance how long the consumer will live, namely 'maxAge' days.
	 * However, you are not allowed to make use of that variable. Your consumer must act as if 'maxAge' was
	 * unknown. The consumer can only consider the daily probability of death, but not maxAge. 
	 */
	public GeneralConsumer(IAgentIdGenerator id, int maxAge, Endowment end, IUtility utility) {
		super(id, maxAge, end, utility);
		this.dailySpendings = new MovingAverage(0.95);
	}
	
	@Override
	protected void trade(Inventory inv, IPriceTakerMarket market) {
		// We hide a relative amount of what is left as a buffer
		Inventory reducedInv = inv.hideRelative(getMoney().getGood(), MONEY_RESERVE);
		super.trade(reducedInv, market);
	}
	
	@Override
	protected void notifySpent(double spendings) {
		dailySpendings.add(spendings);
	}
	
	public double getDailySpendings() {
		return dailySpendings.getAverage();
	}
	
	@Override
	protected double consume(Inventory inv) {
		inv.hideRelative(BasicEconomyConfiguration.POTATOE, POTATOE_RESERVE);
		return super.consume(inv);
	}
	
}

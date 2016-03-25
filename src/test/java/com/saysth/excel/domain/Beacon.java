
package com.saysth.excel.domain;

/**
 * 
 * @author 王辉阳
 *
 * @description beacon测试数据
 *
 * @date 2015年9月20日 上午11:21:23
 */
public class Beacon {
	private String gameScene;
	private String priseName;
	private Double winProbability;
	private Double stock;
	private String acceptance;
	private String createTime;

	public String getGameScene() {
		return gameScene;
	}

	public void setGameScene(String gameScene) {
		this.gameScene = gameScene;
	}

	public String getPriseName() {
		return priseName;
	}

	public void setPriseName(String priseName) {
		this.priseName = priseName;
	}

	public Double getWinProbability() {
		return winProbability;
	}

	public void setWinProbability(Double winProbability) {
		this.winProbability = winProbability;
	}

	public Double getStock() {
		return stock;
	}

	public void setStock(Double stock) {
		this.stock = stock;
	}

	public String getAcceptance() {
		return acceptance;
	}

	public void setAcceptance(String acceptance) {
		this.acceptance = acceptance;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Beacon [gameScene=" + gameScene + ", priseName=" + priseName + ", winProbability=" + winProbability + ", stock=" + stock + ", acceptance=" + acceptance + ", createTime=" + createTime + "]";
	}

}

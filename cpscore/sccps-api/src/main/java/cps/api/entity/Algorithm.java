package cps.api.entity;

/**
 * 实现算法的标准接口
 *
 * @author chenke
 */
public abstract class Algorithm {

	public Algorithm() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Algorithm(CPSInstance cpsInstance) {
		super();
		this.cpsInstance = cpsInstance;
	}

	protected CPSInstance cpsInstance;

	/**
	 * 通过该方法实现算法调用
	 *
	 * @return 返回调用算法结果
	 */
	abstract public String calculate();

	protected CPSInstance getCpsInstance() {
		return cpsInstance;
	}

	public void setCpsInstance(CPSInstance cpsInstance) {
		this.cpsInstance = cpsInstance;
	}
}

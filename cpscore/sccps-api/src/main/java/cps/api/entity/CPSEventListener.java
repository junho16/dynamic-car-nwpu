package cps.api.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public  class CPSEventListener<T extends CPSInstance> extends EntityEventListener<T> {

    private static final Logger logger = LoggerFactory.getLogger(CPSEventListener.class);

	

	@Override
	public void beforeAttributeChange(String name, Object newValue) {
		// TODO Auto-generated method stub
		super.beforeAttributeChange(name, newValue);
		logger.debug("cps实体属性{}变化为{}", name, newValue);
	}

}

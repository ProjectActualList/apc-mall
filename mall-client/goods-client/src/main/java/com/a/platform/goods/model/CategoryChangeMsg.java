package com.a.platform.goods.model;

import java.io.Serializable;

/**
 * 分类变更消息
 *
 * @author weixing.yang
 * @version 1.0
 * @date 2019/11/14 17:35
 */
public class CategoryChangeMsg implements Serializable{

	private static final long serialVersionUID = 6042345706426853823L;
	
	/**
	 * 分类id
	 */
	private Integer categoryId;

	/**
	 * 操作类型
	 */
	private Integer operationType;

	/**
	 * 添加
	 */
	public final static int ADD_OPERATION = 1;

	/**
	 * 修改
	 */
	public final static int UPDATE_OPERATION = 2;

	/**
	 * 删除
	 */
	public final static int DEL_OPERATION = 3;


	public CategoryChangeMsg(Integer categoryId, Integer operationType) {
		super();
		this.categoryId = categoryId;
		this.operationType = operationType;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	@Override
	public String toString() {
		return "CategoryChangeMsg [categoryId=" + categoryId + ", operationType=" + operationType + "]";
	}

}

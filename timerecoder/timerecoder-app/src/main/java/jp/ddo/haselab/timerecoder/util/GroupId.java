package jp.ddo.haselab.timerecoder.util;

public enum GroupId {
    CATEGORY_GROUP_ID(100),
	CATEGORY2_GROUP_ID(101),
	;

	private int groupId;
    private GroupId(int arg) {
	groupId = arg;
    }
    
    public int getGroupId(){
	return groupId;
    }
}
    
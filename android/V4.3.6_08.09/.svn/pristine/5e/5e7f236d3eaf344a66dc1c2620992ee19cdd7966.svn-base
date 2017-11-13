package com.zhisland.im.util;

import org.jivesoftware.smack.util.StringUtils;

public class IdGenerator {

	    /**
	     * Returns the next unique id. Each id made up of a short alphanumeric
	     * prefix along with a unique numeric value.
	     *
	     * @return the next id.
	     */
	    public static synchronized String nextID() {
	        return prefix + Long.toString(id++);
	    }

	    /**
	     * A prefix helps to make sure that ID's are unique across mutliple instances.
	     */
	    private static String prefix = StringUtils.randomString(5);

	    /**
	     * Keeps track of the current increment, which is appended to the prefix to
	     * forum a unique ID.
	     */
	    private static long id = 0;
}

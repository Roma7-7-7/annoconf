package org.annoconf;

/**
 * Created by roma on 3/19/17.
 */
public interface PropertyValueSource {

    boolean hasValue(String key);

    String getValue(String key);

}

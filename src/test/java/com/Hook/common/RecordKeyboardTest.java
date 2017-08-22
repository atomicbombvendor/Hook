package com.Hook.common;

import org.junit.Assert;
import org.junit.Test;

import static java.util.EnumSet.allOf;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by ZZ on 2017/8/22.
 */
public class RecordKeyboardTest {
    @Test
    public void getInstance() throws Exception {
        RecordKeyboard instance = RecordKeyboard.getInstance();
        Assert.assertNotNull(instance);
    }

    @Test
    public void keyDown() throws Exception {
        RecordKeyboard instance = RecordKeyboard.getInstance();
        instance.keyDown(8);
        Integer value = instance.getValue(8);
        assertThat( value, greaterThan(0));
    }

}
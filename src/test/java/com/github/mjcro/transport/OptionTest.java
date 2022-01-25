package com.github.mjcro.transport;

import org.testng.Assert;
import org.testng.annotations.Test;

public class OptionTest {
//    @Test
    public void testMerge() {
        Option[][] chunks = new Option[][]{
                {Option.httpMethod("foo"), Option.httpMethod("bar")},
                null,
                {Option.httpMethod("baz")},
                {Option.httpMethod("xxx"), Option.httpMethod("yyy"), Option.httpMethod("zzz")}
        };

        Option[] merged = Option.merge(chunks);

        Assert.assertEquals(merged.length, 6);
        Assert.assertEquals(merged[0], Option.httpMethod("foo"));
        Assert.assertEquals(merged[1], Option.httpMethod("bar"));
        Assert.assertEquals(merged[2], Option.httpMethod("baz"));
        Assert.assertEquals(merged[3], Option.httpMethod("xxx"));
        Assert.assertEquals(merged[4], Option.httpMethod("yyy"));
        Assert.assertEquals(merged[5], Option.httpMethod("zzz"));
    }

    @Test
    public void testMergeNone() {
        Assert.assertNotNull(Option.merge((Option[][]) null));
        Assert.assertNotNull(Option.merge((Option[]) null));
        Assert.assertNotNull(Option.merge(new Option[0]));
        Assert.assertNotNull(Option.merge());
    }
}
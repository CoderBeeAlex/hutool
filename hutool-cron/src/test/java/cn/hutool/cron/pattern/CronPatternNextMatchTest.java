package cn.hutool.cron.pattern;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class CronPatternNextMatchTest {

	@Test
	public void nextMatchAllAfterTest() {
		// 匹配所有，返回下一秒的时间
		CronPattern pattern = new CronPattern("* * * * * * *");
		DateTime date = DateUtil.truncate(DateUtil.date(), DateField.SECOND);
		Calendar calendar = pattern.nextMatchAfter(date.toCalendar());
		Assert.assertEquals(date.getTime(), DateUtil.date(calendar).getTime());

		// 匹配所有分，返回下一分钟
		pattern = new CronPattern("0 * * * * * *");
		date = DateUtil.parse("2022-04-08 07:44:16");
		//noinspection ConstantConditions
		calendar = pattern.nextMatchAfter(date.toCalendar());
		Assert.assertEquals(DateUtil.parse("2022-04-08 07:45:00"), DateUtil.date(calendar));
	}

	@Test
	public void nextMatchAfterTest(){
		CronPattern pattern = new CronPattern("23 12 * 12 * * *");

		// 时间正常递增
		//noinspection ConstantConditions
		Calendar calendar = pattern.nextMatchAfter(
				DateUtil.parse("2022-04-12 09:12:12").toCalendar());

		Assert.assertTrue(pattern.match(calendar, true));
		Assert.assertEquals("2022-04-12 09:12:23", DateUtil.date(calendar).toString());

		// 秒超出规定值的最大值，分+1，秒取最小值
		//noinspection ConstantConditions
		calendar = pattern.nextMatchAfter(
				DateUtil.parse("2022-04-12 09:09:24").toCalendar());
		Assert.assertTrue(pattern.match(calendar, true));
		Assert.assertEquals("2022-04-12 09:12:23", DateUtil.date(calendar).toString());

		// 秒超出规定值的最大值，分不变，小时+1，秒和分使用最小值
		//noinspection ConstantConditions
		calendar = pattern.nextMatchAfter(
				DateUtil.parse("2022-04-12 09:12:24").toCalendar());
		Assert.assertTrue(pattern.match(calendar, true));
		Assert.assertEquals("2022-04-12 10:12:23", DateUtil.date(calendar).toString());

		// 天超出规定值的最大值，月+1，天、时、分、秒取最小值
		//noinspection ConstantConditions
		calendar = pattern.nextMatchAfter(
				DateUtil.parse("2022-04-13 09:12:24").toCalendar());
		Assert.assertTrue(pattern.match(calendar, true));
		Assert.assertEquals("2022-05-12 00:12:23", DateUtil.date(calendar).toString());
	}
}

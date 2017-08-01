package com.dewly.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;

public class QuartzUtil {
	/**
	 * 根据corn表达式计算下一次执行的时间
	 * @param cron
	 * @return
	 * @throws ParseException
	 */
	public String nextDateTime(String cron) throws ParseException {
			CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
			cronTriggerImpl.setCronExpression(cron);
			List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 1);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (dates != null && dates.size() > 0) {
				return df.format(dates.get(0));
			}
			return null;
	}

	public String nextDateTime(Date startTime, Date endTime, int repeatCount, long repeatInterval) {
		SimpleTriggerImpl trigger = new SimpleTriggerImpl();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		trigger.setStartTime(startTime);
		trigger.setEndTime(endTime);
		trigger.setRepeatCount(repeatCount);
		trigger.setRepeatInterval(repeatInterval);
		List<Date> dates = TriggerUtils.computeFireTimes(trigger, null, 100);
		if (dates != null && dates.size() > 0) {
			return df.format(dates.get(0));
		}
		return null;

	}
}

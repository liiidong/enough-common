package com.enough.common.utils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lidong
 * @apiNote 时间工具类
 * @since 2020/07/09
 */
@Slf4j
public class DateUtil {
    private String pattern = "yyyy-MM-dd";

    public static DateUtil of() {
        return new DateUtil();
    }

    public DateUtil pattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public LocalDate date(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        return LocalDate.parse(date);
    }

    /**
     * 昨天
     *
     * @param date
     * @return
     */
    public LocalDate lastDay(LocalDate date) {
        return date.minusDays(1);
    }

    public LocalDate offsetDay(LocalDate date, int offset) {
        return date.minusDays(offset);
    }

    /**
     * 上周
     *
     * @param date
     * @return
     */
    public LocalDate lastWeek(LocalDate date) {
        return date.minusWeeks(1);
    }

    public LocalDate offsetWeek(LocalDate date, int offset) {
        return date.minusWeeks(offset);
    }

    /**
     * 上月
     *
     * @param date
     * @return
     */
    public LocalDate lastMonth(LocalDate date) {
        return date.minusMonths(1);
    }

    public LocalDate offsetMonth(LocalDate date, int offset) {
        return date.minusMonths(offset);
    }

    /**
     * 去年
     *
     * @param date
     * @return
     */
    public LocalDate lastYear(LocalDate date) {
        return date.minusYears(1);
    }

    public LocalDate offsetYear(LocalDate date, int offset) {
        return date.minusYears(offset);
    }

    /**
     * 获取本年第几天
     *
     * @param date
     * @return
     */
    public int day(LocalDate date) {
        return date.getDayOfYear();
    }

    /**
     * 获取本年第几周
     *
     * @param date
     * @return
     */
    public int week(LocalDate date) {
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
        return date.get(weekFields.weekOfYear());
    }

    /**
     * 获取本年第几月
     *
     * @param date
     * @return
     */
    public int month(LocalDate date) {
        return date.getMonthValue();
    }

    /**
     * 获取年份
     *
     * @param date
     * @return
     */
    public int year(LocalDate date) {
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
        return date.get(weekFields.weekBasedYear());
    }

    /**
     * 计算两个时间相差几天，按日周月区分
     *
     * @param start
     * @param end
     * @param statisticsType
     * @return
     */
    public long gap(String start, String end, StatisticsType statisticsType) {
        if (StringUtils.isBlank(start) || StringUtils.isBlank(end)) {
            return 0;
        }
        int gap;
        switch (statisticsType) {
        case DAY:
            gap = LocalDate.parse(end).getDayOfYear() - LocalDate.parse(start).getDayOfYear();
            break;
        case WEEK:
            WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
            gap = LocalDate.parse(end).get(weekFields.weekOfYear()) - LocalDate.parse(start).get(weekFields.weekOfYear());
            break;
        case MONTH:
            gap = LocalDate.parse(end).getMonthValue() - LocalDate.parse(start).getMonthValue();
            break;
        default:
            gap = 0;
            break;
        }
        return gap;
    }

    public String concat(LocalDate from, LocalDate to) {
        return from.toString().concat(" ~ ").concat(to.toString());
    }

    public enum StatisticsType {
        DAY, WEEK, MONTH;

        public static StatisticsType statisticsType(TimeDimensionType timeDimensionType) {
            StatisticsType type;
            switch (timeDimensionType) {
            case WEEK_OF_YEAR:
                type = WEEK;
                break;
            case MONTH_OF_YEAR:
                type = MONTH;
                break;
            default:
                type = DAY;
            }
            return type;
        }

        public static TimeDimensionType timeType(StatisticsType statisticsType) {
            TimeDimensionType type;
            switch (statisticsType) {
            case WEEK:
                type = TimeDimensionType.WEEK_OF_YEAR;
                break;
            case MONTH:
                type = TimeDimensionType.MONTH_OF_YEAR;
                break;
            default:
                type = TimeDimensionType.DATA_DAY;
            }
            return type;
        }
    }

    @Data
    public static class NatureTime {
        private Integer day;
        private Integer week;
        private Integer month;
        private String start;
        private String end;

        public String period() {
            if (StringUtils.equalsIgnoreCase(start, end)) {
                return getStart();
            }
            return getStart().concat(" ~ ").concat(getEnd());
        }
    }
    public enum TimeDimensionType {

        DATA_DAY("data_day"), WEEK_OF_YEAR("week_of_year"), MONTH_OF_YEAR("month_of_year"), SUMMARY("summary");
        @Getter
        @Setter
        private String type;

        TimeDimensionType(String type) {
            this.type = type;
        }

        public static TimeDimensionType get(String type) {
            for (TimeDimensionType value : TimeDimensionType.values()) {
                if (value.type.equalsIgnoreCase(type)) {
                    return value;
                }
            }
            return null;
        }

        public static boolean is(TimeDimensionType type) {
            if (type != null) {
                return Arrays.asList(TimeDimensionType.values()).contains(type);
            }
            return false;
        }

        public static Object natureTimeAlias(String type, Object natureTime, Object value) {
            TimeDimensionType timeDimensionType = get(type);
            if (timeDimensionType != null) {
                Object natureTimeAlias = natureTime;
                switch (timeDimensionType) {
                case SUMMARY:
                    natureTimeAlias = "总计(" + value + ")";
                    break;
                case WEEK_OF_YEAR:
                    if (value instanceof String && ArrayUtils.isNotEmpty(String.valueOf(value).split("~"))) {
                        natureTimeAlias = String.valueOf(value).split("~")[0] + "这一周";
                    } else {
                        natureTimeAlias = "当年第" + natureTime + "周";
                    }
                    break;
                case MONTH_OF_YEAR:
                    natureTimeAlias = natureTime + "月";
                    break;
                default:
                    break;
                }
                return natureTimeAlias;
            }
            return natureTime;
        }
    }

    /**
     * 根据自然时间获取对应的时间段
     *
     * @param statisticsType
     * @param startDate
     * @param endDate
     * @param natureTime
     * @return
     */
    public String[] getNatureTimePeriod(StatisticsType statisticsType, String startDate, String endDate, List <String> natureTime) {
        List <NatureTime> natureTimes = parseNatureTimes(statisticsType, startDate, endDate);
        if (CollectionUtils.isEmpty(natureTime)) {
            return null;
        }
        String[] natureTimePeriod = new String[2];
        List <String> dates = new ArrayList <>();
        if (statisticsType == StatisticsType.DAY) {
            natureTimePeriod[0] = natureTime.get(0);
            natureTimePeriod[1] = natureTime.get(natureTime.size() - 1);
            return natureTimePeriod;
        }
        if (statisticsType == StatisticsType.WEEK) {
            natureTimes = natureTimes.stream().filter(p -> natureTime.contains(p.getWeek().toString())).collect(Collectors.toList());
            for (NatureTime time : natureTimes) {
                dates.add(time.getStart());
                dates.add(time.getEnd());
            }
        }
        if (statisticsType == StatisticsType.MONTH) {
            natureTimes = natureTimes.stream().filter(p -> natureTime.contains(p.getMonth().toString())).collect(Collectors.toList());
            for (NatureTime time : natureTimes) {
                dates.add(time.getStart());
                dates.add(time.getEnd());
            }
        }
        sort(dates);
        natureTimePeriod[0] = dates.get(0);
        natureTimePeriod[1] = dates.get(dates.size() - 1);
        return natureTimePeriod;
    }

    /**
     * 根据自然时间获取对应的时间段
     *
     * @param statisticsType
     * @param startDate
     * @param endDate
     * @param natureTime
     * @return
     */
    public String[] getNatureTimePeriod(StatisticsType statisticsType, String startDate, String endDate, List <String> natureTime, long gap) {
        List <NatureTime> natureTimes = parseNatureTimes(statisticsType, startDate, endDate);
        if (CollectionUtils.isEmpty(natureTime)) {
            return null;
        }
        String[] natureTimePeriod = new String[2];
        List <String> dates = new ArrayList <>();
        if (statisticsType == StatisticsType.DAY) {
            //todo
            natureTimePeriod[0] = getOffsetDate(StatisticsType.timeType(statisticsType), natureTime.get(0), gap);
            natureTimePeriod[1] = getOffsetDate(StatisticsType.timeType(statisticsType), natureTime.get(natureTime.size() - 1), gap);
            return natureTimePeriod;
        }
        if (statisticsType == StatisticsType.WEEK) {
            natureTimes = natureTimes.stream().filter(p -> natureTime.contains(String.valueOf((p.getWeek() - gap)))).collect(Collectors.toList());
            for (NatureTime time : natureTimes) {
                dates.add(time.getStart());
                dates.add(time.getEnd());
            }
        }
        if (statisticsType == StatisticsType.MONTH) {
            natureTimes = natureTimes.stream().filter(p -> natureTime.contains(String.valueOf(p.getMonth() - gap))).collect(Collectors.toList());
            for (NatureTime time : natureTimes) {
                dates.add(time.getStart());
                dates.add(time.getEnd());
            }
        }
        if (CollectionUtils.isEmpty(dates)) {
            return null;
        }
        sort(dates);
        natureTimePeriod[0] = dates.get(0);
        natureTimePeriod[1] = dates.get(dates.size() - 1);
        return natureTimePeriod;
    }

    /**
     * 解析自然时间
     *
     * @param statisticsType
     * @param startDate
     * @param endDate
     * @return
     */
    public List <NatureTime> parseNatureTimes(StatisticsType statisticsType, String startDate, String endDate) {
        List <NatureTime> natureTimes = new ArrayList <>();
        if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
            return natureTimes;
        }
        LocalDate sLocalDate = LocalDate.parse(startDate);
        LocalDate eLocalDate = LocalDate.parse(endDate);
        if (sLocalDate.getDayOfYear() > eLocalDate.getDayOfYear()) {
            return natureTimes;
        }
        boolean newTime = true;
        if (statisticsType == StatisticsType.WEEK) {
            NatureTime natureTime = new NatureTime();
            while (sLocalDate.getDayOfYear() <= eLocalDate.getDayOfYear()) {
                //起始时间同一周时
                if (week(eLocalDate) == week(sLocalDate)) {
                    NatureTime sameWeek = startWeekTime(week(sLocalDate), sLocalDate.toString());
                    sameWeek.setEnd(eLocalDate.toString());
                    natureTimes.add(sameWeek);
                }
                //构造新一周对象
                if (newTime) {
                    natureTime = startWeekTime(week(sLocalDate), sLocalDate.toString());
                }
                //准备新一周
                if (sLocalDate.getDayOfWeek().getValue() == 7) {
                    natureTime.setEnd(sLocalDate.toString());
                    natureTimes.add(natureTime);
                    newTime = true;
                } else {
                    newTime = false;
                }
                sLocalDate = sLocalDate.plusDays(1);
            }
        } else if (statisticsType == StatisticsType.MONTH) {
            NatureTime natureTime = new NatureTime();
            while (sLocalDate.getDayOfYear() <= eLocalDate.getDayOfYear()) {
                if (newTime) {
                    natureTime = startMonthTime(month(sLocalDate), sLocalDate.toString());
                }
                if (sLocalDate.getDayOfYear() == eLocalDate.getDayOfYear()) {
                    natureTime.setEnd(eLocalDate.toString());
                    natureTimes.add(natureTime);
                }
                if (natureTime.getMonth().compareTo(month(sLocalDate)) != 0) {
                    //跨月份了，sLocalDate应减去一天
                    sLocalDate = sLocalDate.plusDays(-1);
                    natureTime.setEnd(sLocalDate.toString());
                    natureTimes.add(natureTime);
                    newTime = true;
                } else {
                    newTime = false;
                }
                sLocalDate = sLocalDate.plusDays(1);
            }
        } else if (statisticsType == StatisticsType.DAY) {
            List <String> betweenList = DateUtil.of().getBetweenDate(startDate, endDate);
            betweenList.forEach(s -> {
                NatureTime natureTime = new NatureTime();
                natureTime.setDay(LocalDate.parse(s).getDayOfYear());
                natureTime.setStart(s);
                natureTime.setEnd(s);
                natureTimes.add(natureTime);
            });
        }
        return natureTimes;
    }

    private NatureTime startWeekTime(int week, String time) {
        NatureTime natureTime = new NatureTime();
        natureTime.setWeek(week);
        natureTime.setStart(time);
        return natureTime;
    }

    private NatureTime startMonthTime(int month, String time) {
        NatureTime natureTime = new NatureTime();
        natureTime.setMonth(month);
        natureTime.setStart(time);
        return natureTime;
    }

    /**
     * 解析时间段，根据数据库查询的自然日、周、月获取对应的日期字符串
     *
     * @param timeDimensionType 时间类型枚举
     * @param from              开始时间
     * @param to                结束时间
     * @param timeKey           时间节点值eg. 2020-09-09
     * @return
     */
    public String getTimePeriod(TimeDimensionType timeDimensionType, String from, String to, String timeKey) {
        String timePeriod = null;
        if (timeDimensionType == TimeDimensionType.WEEK_OF_YEAR) {
            Optional <NatureTime> opt = DateUtil
                    .of().parseNatureTimes(StatisticsType.WEEK, from, to).stream()
                    .filter(f -> f.getWeek().toString().equals(timeKey)).findFirst();
            if (opt.isPresent()) {
                timePeriod = opt.get().period();
            }
        } else if (timeDimensionType == TimeDimensionType.MONTH_OF_YEAR) {
            Optional <NatureTime> opt = DateUtil
                    .of().parseNatureTimes(StatisticsType.MONTH, from, to).stream()
                    .filter(f -> f.getMonth().toString().equals(timeKey)).findFirst();
            if (opt.isPresent()) {
                timePeriod = opt.get().period();
            }
        } else if (timeDimensionType == TimeDimensionType.DATA_DAY) {
            timePeriod = timeKey;
        } else if (timeDimensionType == TimeDimensionType.SUMMARY) {
            timePeriod = from + " ~ " + to;
        }
        return timePeriod;
    }

    public String getOffsetDate(TimeDimensionType timeDimensionType, String dateValue, long gap) {
        String offsetTime = null;
        switch (timeDimensionType) {
        case DATA_DAY:
            offsetTime = LocalDate.parse(dateValue).plusDays(gap).toString();
            break;
        case WEEK_OF_YEAR:
        case MONTH_OF_YEAR:
            offsetTime = String.valueOf((Integer.parseInt(dateValue) + gap));
            break;
        default:
            offsetTime = "";
            break;
        }
        return offsetTime;
    }

    /**
     * 返回对应的时间段
     *
     * @param timeDimensionType 类型：日、周、月
     * @param from              开始时间
     * @param to                结束时间
     * @param difference        对比时间段的差值
     * @return
     */
    public List <String> getCorrespondingTime(TimeDimensionType timeDimensionType, String from, String to, int difference) {
        List <String> correspondingTimes = new ArrayList <>();
        try {
            if (StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
                return correspondingTimes;
            }
            Calendar sCalendar = calendar(from);
            Calendar eCalendar = calendar(to);
            if (sCalendar.getTime().getTime() > eCalendar.getTime().getTime()) {
                return correspondingTimes;
            }
            DateFormat dateFormat = new SimpleDateFormat(this.pattern);
            Calendar calendarFrom = calendar(from);
            Calendar calendarTo = calendar(to);
            if (timeDimensionType == TimeDimensionType.WEEK_OF_YEAR) {
                calendarFrom.add(Calendar.DAY_OF_YEAR, difference);
                List <NatureTime> natureTimes = this
                        .parseNatureTimes(StatisticsType.statisticsType(timeDimensionType), dateFormat.format(calendarFrom.getTime()),
                                dateFormat.format(calendarTo.getTime()));
                //对比时间 按 自然周 差值
                int diffWeek = difference / 7;
                natureTimes.forEach(natureTime -> correspondingTimes.add(String.valueOf((natureTime.getWeek() + diffWeek))));
            } else if (timeDimensionType == TimeDimensionType.MONTH_OF_YEAR) {
                while (sCalendar.getTime().getTime() <= eCalendar.getTime().getTime()) {
                    calendarFrom.add(Calendar.MONTH, difference);
                    correspondingTimes.add(dateFormat.format(calendarFrom.getTime()));
                    sCalendar.add(Calendar.MONTH, 1);
                    calendarFrom = calendar(dateFormat.format(sCalendar.getTime()));
                }
            } else if (timeDimensionType == TimeDimensionType.DATA_DAY) {
                while (sCalendar.getTime().getTime() <= eCalendar.getTime().getTime()) {
                    calendarFrom.add(Calendar.DAY_OF_YEAR, difference);
                    correspondingTimes.add(dateFormat.format(calendarFrom.getTime()));
                    sCalendar.add(Calendar.DAY_OF_YEAR, 1);
                    calendarFrom = calendar(dateFormat.format(sCalendar.getTime()));
                }
            } else if (timeDimensionType == TimeDimensionType.SUMMARY) {

            }
        } catch (ParseException e) {
            log.error("时间解析异常，", e);
        }
        return correspondingTimes;
    }

    /**
     * 根据时间获取日历对象
     *
     * @param time
     * @return
     * @throws ParseException
     */
    private Calendar calendar(String time) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(this.pattern);
        Date sDate = dateFormat.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(sDate);
        return calendar;
    }

    public void sort(List <String> dates) {
        dates.sort(Comparator.comparing(LocalDate::parse));
    }

    /**
     * 获取两个时间段的交集
     *
     * @param sTime1 开始时间1
     * @param eTime1 结束时间1
     * @param sTime2 开始时间2
     * @param eTime2 结束时间2
     * @return
     * @throws Exception
     * @since 2020919 废弃的方法，请使用{@code DateUtil.of().intersectionTime()}
     */
    @Deprecated
    public String[] getTimeInterval(String sTime1, String eTime1, String sTime2, String eTime2) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        long lst = dateFormat.parse(sTime1).getTime();
        long let = dateFormat.parse(eTime1).getTime();

        long rst = dateFormat.parse(sTime2).getTime();
        long ret = dateFormat.parse(eTime2).getTime();

        if (lst > let || rst > ret) {
            throw new Exception("起始时间不能大于结束时间");
        }

        long[] a = {lst, let, rst, ret};
        //从小到大排序，取第二、第三计算
        Arrays.sort(a);
        System.out.println(dateFormat.format(new Date(a[1])));
        System.out.println(dateFormat.format(new Date(a[2])));
        String[] arr = new String[2];
        if (rst > lst) {
            arr[0] = dateFormat.format(new Date(a[1]));
            arr[1] = dateFormat.format(new Date(a[2]));
        } else if (rst == let) {
            arr[0] = dateFormat.format(new Date(a[1]));
            arr[1] = dateFormat.format(new Date(a[3]));
        } else {
            arr[0] = dateFormat.format(new Date(a[0]));
            arr[1] = dateFormat.format(new Date(a[2]));
        }
        return arr;
    }

    public String[] intersectionTime(String sTime1, String eTime1, String sTime2, String eTime2) {
        List <String> intersectionTime = getBetweenDate(sTime1, eTime1).stream().filter(f -> getBetweenDate(sTime2, eTime2).contains(f))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(intersectionTime)) {
            return null;
        }
        String[] arr = new String[2];
        arr[0] = intersectionTime.get(0);
        arr[1] = intersectionTime.get(intersectionTime.size() - 1);
        return arr;
    }

    /**
     * 获取两个时间段的所有日期
     *
     * @param start
     * @param end
     * @return
     */
    public List <String> getBetweenDate(String start, String end) {
        List <String> betweenList = new ArrayList <>();

        LocalDate startDate = date(start);
        LocalDate endDate = date(end);

        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance < 0) {
            return betweenList;
        }
        Stream.iterate(startDate, d -> d.plusDays(1)).limit(distance + 1).forEach(f -> betweenList.add(f.toString()));
        return betweenList;
    }

    public String nowDate() {
        return LocalDate.now().toString();
    }

    public String nowTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public int weekOfMonth(String start, String end, int weekOfYear) {
        List <NatureTime> natureTimes = parseNatureTimes(StatisticsType.WEEK, start, end);
        Optional <NatureTime> opt = natureTimes.stream().filter(p -> p.getWeek() == weekOfYear).findFirst();
        return opt.map(natureTime -> natureTimes.indexOf(natureTime) + 1).orElse(0);
    }

    public static void main(String[] args) {
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
        LocalDate date = LocalDate.parse("2020-10-20");
        LocalDate date1 = LocalDate.parse("2021-10-20");
        System.out.println(date.get(weekFields.weekOfYear()));
        System.out.println(of().year(date));
        System.out.println(date1.get(weekFields.weekOfYear()));
        System.out.println(date1.get(weekFields.weekBasedYear()));
        System.out.println(date.get(weekFields.weekOfMonth()));

        System.out.println(of().weekOfMonth("2020-08-01", "2020-08-31", 36));

        String time = "2020-08-01";
        System.out.println(Arrays.asList(time.split("~")).toString());

    }
}
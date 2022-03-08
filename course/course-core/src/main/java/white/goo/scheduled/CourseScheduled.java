package white.goo.scheduled;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import white.goo.constant.CourseKeys;
import white.goo.constant.CourseStatusEnum;
import white.goo.entity.Course;
import white.goo.service.CourseService;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableScheduling
public class CourseScheduled {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CourseService courseService;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void initCourse() {
        RLock courseScheduled = redissonClient.getLock("CourseScheduled");
        if (courseScheduled.tryLock()) {
            List<Course> list = courseService.list();
            Date now = new Date();
            for (Course course : list) {
                int compare = DateUtil.compare(now, course.getDate());
                int compare1 = DateUtil.compare(now, course.getEndDate());
                course.setStatus(compare < 0 ? CourseStatusEnum.NOT_START : compare1 < 0 ? CourseStatusEnum.START : CourseStatusEnum.END);
                RAtomicLong atomicLong = redissonClient.getAtomicLong(CourseKeys.COURSE_AMOUNT + course.getId());
                List<String> students = JSON.parseArray(course.getStudents(), String.class);

                if (!atomicLong.isExists() && Objects.nonNull(students) && !(course.getCourseAmount() == students.size())) {
                    atomicLong.set(course.getCourseAmount() - students.size());
                }
                courseService.updateById(course);
            }
        }
    }

}

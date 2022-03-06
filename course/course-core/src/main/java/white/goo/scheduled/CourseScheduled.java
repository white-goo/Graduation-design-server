package white.goo.scheduled;

import cn.hutool.core.date.DateUtil;
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

@Configuration
@EnableScheduling
public class CourseScheduled {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CourseService courseService;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void initCourse(){
        RLock courseScheduled = redissonClient.getRedLock(redissonClient.getLock("CourseScheduled"));
        if (courseScheduled.tryLock()) {
            List<Course> list = courseService.list();
            Date now = new Date();
            for (Course course : list) {
                int compare = DateUtil.compare(now, course.getDate());
                int compare1 = DateUtil.compare(now, course.getEndDate());
                course.setStatus(compare < 0 ? CourseStatusEnum.NOT_START : compare1 < 0 ? CourseStatusEnum.START : CourseStatusEnum.END);
                if (CourseStatusEnum.START == course.getStatus()){
                    RAtomicLong atomicLong = redissonClient.getAtomicLong(CourseKeys.COURSE_AMOUNT + course.getId());
                    if(!atomicLong.isExists()){
                        atomicLong.set(course.getCourseAmount());
                    }
                }
                courseService.updateById(course);
            }
        }
    }

}

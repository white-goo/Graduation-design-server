package white.goo.config;

import com.baidu.fsg.uid.worker.dao.WorkerNodeDAO;
import com.baidu.fsg.uid.worker.entity.WorkerNodeEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class WorkerNodeDaoImpl extends ServiceImpl<WorkerNodeMapper, WorkerNodeEntity>
        implements WorkerNodeDAO {
    @Override
    public WorkerNodeEntity getWorkerNodeByHostPort(String host, String port) {
        QueryWrapper<WorkerNodeEntity> wrapper = new QueryWrapper<WorkerNodeEntity>().eq("host", host).eq("port", port);
        return super.getOne(wrapper);
    }

    @Override
    public void addWorkerNode(WorkerNodeEntity workerNodeEntity) {
        workerNodeEntity.setModified(new Date());
        workerNodeEntity.setCreated(new Date());
        super.save(workerNodeEntity);
    }
}

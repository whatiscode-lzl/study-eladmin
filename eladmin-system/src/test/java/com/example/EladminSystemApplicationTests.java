package com.example;

import com.example.domain.Log;
import com.example.modules.system.domain.Dept;
import com.example.modules.system.repository.DeptRepository;
import com.example.modules.system.service.dto.DeptDto;
import com.example.modules.system.service.mapper.DeptMapper;
import com.example.repository.LogRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/31
 * Time: 15:21
 * Description: No Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EladminSystemApplicationTests {

    @Autowired
    DeptRepository deptRepository;

    @Autowired
    DeptMapper deptMapper;

    @Autowired
    LogRepository repository;

    @Test
    public void test01(){
        Set<Dept> depts = deptRepository.findByRoles_id(new Long(1));
        for (Dept dept: depts){
            System.out.println("dept:"+dept.toString());
        }
        System.out.println("set.size=="+depts.size());
        Iterator<Dept> iterator = depts.iterator();
        while (iterator.hasNext()){
            Dept next = iterator.next();
            DeptDto deptDto = deptMapper.toDto(next);
            System.out.println("deptDto:"+deptDto);
        }
    }

    @Test
    public void testLog(){
        Log log = new Log();
        log.setAddress("test");
        log.setBrowser("test");
        log.setExceptionDetail("这是测试异常的消息".getBytes());
        repository.save(log);
    }
}

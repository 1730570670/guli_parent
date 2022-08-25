package com.edu.eduservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.commonutils.R;
import com.edu.eduservice.pojo.EduTeacher;
import com.edu.eduservice.pojo.vo.QueryEduTeacher;
import com.edu.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @CreateTime: 2022-08-15
 * 讲师
 */
@RestController
@Api(description = "讲师管理")
@RequestMapping("/eduservice/teacher")
@Slf4j
@CrossOrigin
public class EduTeacherController {

    /**
     * 将EduTeacherService注入使用
     */
    @Autowired
    private EduTeacherService eduTeacherService;

    @ApiOperation("条件查询讲师列表")
    @GetMapping("/pageTeacherByWrapper/{current}/{limit}")
    public R pageTeacherByWrapper(@ApiParam(value = "current", name = "需要查询的当前页", required = true) @PathVariable Integer current,
                                  @ApiParam(value = "limit", name = "查询的数量", required = true) @PathVariable Integer limit,
                                  QueryEduTeacher queryEduTeacher) {
        Page<EduTeacher> eduTeacherPage = new Page<>(current, limit);//分页查询
        //条件构造器
        LambdaQueryWrapper<EduTeacher> eduTeacherLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //按照序号进行排序
        eduTeacherLambdaQueryWrapper.orderByAsc(EduTeacher::getSort)
                .orderByAsc(EduTeacher::getLevel);
        //创建时间的范围(有一个为空则作废)
        if (queryEduTeacher.getStartTime() != null && queryEduTeacher.getEndTime() != null) {
            eduTeacherLambdaQueryWrapper.between(EduTeacher::getGmtCreate, queryEduTeacher.getStartTime(), queryEduTeacher.getEndTime());
        }
        //如果名字不为空,名字模糊查询
        if (queryEduTeacher.getName() != "") {
            eduTeacherLambdaQueryWrapper.like(EduTeacher::getName, queryEduTeacher.getName());
        }
        //如果头衔不为空则添加进条件
        if (queryEduTeacher.getLevel() != null) {
            eduTeacherLambdaQueryWrapper.eq(EduTeacher::getLevel, queryEduTeacher.getLevel());
        }

        //查询
        Page<EduTeacher> page = eduTeacherService.page(eduTeacherPage, eduTeacherLambdaQueryWrapper);
        return R.ok().data("item", eduTeacherPage);
    }


    /**
     * 根据id删除Teacher
     *
     * @param id
     * @return R
     */
    @ApiOperation(value = "逻辑删除所有列表")
    @DeleteMapping("/{id}")
    public R removeTeacher(
            @ApiParam(name = "id", value = "需要删除的讲师ID", required = true)
            @PathVariable String id) {
        boolean isDelete = eduTeacherService.removeById(id);
        if (isDelete) {
            //删除成功
            return R.ok().data("msg", "删除成功");
        } else {
            //删除失败
            return R.error().data("msg", "删除失败");
        }
    }


    /**
     * 根据ID查询讲师信息
     *
     * @return
     */
    @ApiOperation("根据ID查询讲师信息")
    @GetMapping("/queryById/{id}")
    public R queryById(@ApiParam(value = "id", name = "需要查询讲师的ID", required = true) @PathVariable String id) {
        // 创建条件构造器
        LambdaQueryWrapper<EduTeacher> eq = new LambdaQueryWrapper<>();
        eq.eq(EduTeacher::getId, id);//根据ID查询讲师信息
        EduTeacher oneTeacher = eduTeacherService.getOne(eq);//获取相关信息信息
        //信息不为空,返回信息
        if (oneTeacher != null) {
            return R.ok().data("item", oneTeacher);
        }
        //信息为空,返回出去
        return R.error().message("没有相关信息!");
    }


    /**
     * 根据id修改教师信息
     *
     * @param eduTeacher
     * @return R
     */
    @ApiOperation("根据id修改教师")
    @PutMapping("/updateTeacher")
    public R updateTeacher(@ApiParam(value = "eduTeacher", name = "根据id修改讲师信息", required = true)
                           @RequestBody EduTeacher eduTeacher) {
        boolean isUpdate = eduTeacherService.updateById(eduTeacher);
        //如果修改成功
        if (isUpdate) {
            return R.ok().message("修改成功!");
        }
        //修改失败
        return R.error().message("修改失败!");
    }

    /**
     * 添加讲师
     *
     * @return R
     */
    @ApiOperation("添加讲师")
    @PostMapping("/saveTeacher")
    public R saveTeacher(@ApiParam(value = "eduTeacher", name = "添加讲师", required = true)
                         @RequestBody EduTeacher eduTeacher) {
        LambdaQueryWrapper<EduTeacher> eq = new LambdaQueryWrapper<EduTeacher>();
        eq.eq(EduTeacher::getName, eduTeacher.getName());
        EduTeacher oneTeacher = eduTeacherService.getOne(eq);
        // 讲师存在
        if (oneTeacher != null) {
            return R.error().message(eduTeacher.getName() + "讲师已存在!");
        }
        // 讲师不存在,添加讲师
        boolean save = eduTeacherService.save(eduTeacher);
        if (save) {
            return R.ok().message("添加成功!");
        }
        return R.error().message("添加失败!");
    }
}

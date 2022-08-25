package com.edu.eduservice.listen;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.edu.eduservice.pojo.EduSubject;
import com.edu.eduservice.pojo.excel.ExcelSubject;
import com.edu.eduservice.service.EduSubjectService;

import java.util.List;
import java.util.Map;

/**
 * @CreateTime: 2022-08-23
 * excel文件监听
 */
public class ExcelObjectListen extends AnalysisEventListener<ExcelSubject> {

    /**
     * 通过构造注入属性(交由Spring管理,导致冲突,手动注入)
     */
    private EduSubjectService eduSubjectService;

    public ExcelObjectListen(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        System.out.println(headMap.get(0));//一级分类表头
        System.out.println(headMap.get(1));//二级分类表头
    }

    @Override
    public void invoke(ExcelSubject excelSubject, AnalysisContext analysisContext) {
        String title = excelSubject.getTitle();//一级分类
        String subject = excelSubject.getSubject();//二级分类
        EduSubject isSave = searchIsSaveTitle(title);//判断数据库中是否存在一级标题

        //不存在一级标题
        if (isSave == null) {
            LambdaQueryWrapper<EduSubject> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByAsc(EduSubject::getSort);// 升序排序
            queryWrapper.eq(EduSubject::getParentId, "0");// eq一级元素
            List<EduSubject> list = eduSubjectService.list(queryWrapper);
            Integer sort = (Integer) list.get(list.size() - 1).getSort();// 获取一级分类最后的排序
            EduSubject eduSubject = new EduSubject();// 创建对象

            eduSubject.setParentId("0");// 标示为一级标题
            eduSubject.setTitle(title);// 设置一级标题名称
            eduSubject.setSort(sort + 1);// 排序在最后面(原来最后的基础上 +1)
            eduSubjectService.save(eduSubject);// 保存一级标题


            EduSubject sj = new EduSubject();// 创建对象
            sj.setSort(eduSubject.getSort());// 设置排序
            sj.setTitle(subject);// 设置二级标题
            sj.setParentId(eduSubject.getId());//设置parentID
            eduSubjectService.save(sj);//保存二级标题
        } else {//一级分类存在,添加二级分类
            //获取二级分类所在一级分类的parentID
            LambdaQueryWrapper<EduSubject> eduSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();//条件构造器
            //查询一级分类信息
            eduSubjectLambdaQueryWrapper.eq(EduSubject::getTitle, title);//eq一级分类
            EduSubject eduSubject = eduSubjectService.getOne(eduSubjectLambdaQueryWrapper);//查询一级分类信息
            String parentId = eduSubject.getId();//获取二级分类的parentID
            Integer sort = eduSubject.getSort();//获取一级分类的排序,二级分类与一级分类排序一致
            LambdaQueryWrapper<EduSubject> isSaveWrapper = new LambdaQueryWrapper<>();
            isSaveWrapper.eq(EduSubject::getParentId, parentId);//eqparentID
            isSaveWrapper.eq(EduSubject::getTitle, subject);//eq二级分类
            //查询二级分类是否存在一级分类
            EduSubject one = eduSubjectService.getOne(isSaveWrapper);
            // 如果one没找到二级分类则添加
            if (one == null) {
                EduSubject ej = new EduSubject();
                ej.setTitle(subject);//设置标题
                ej.setSort(sort);//设置排序
                ej.setParentId(parentId);//设置parentID
                eduSubjectService.save(ej);//保存
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("读取完毕");
    }


    /**
     * 查找数据库,查看是否存在一级分类(parent_id:0)
     *
     * @return
     */
    public EduSubject searchIsSaveTitle(String title) {
        LambdaQueryWrapper<EduSubject> eduSubjectLambdaQueryWrapper = new LambdaQueryWrapper<>();//条件构造器
        eduSubjectLambdaQueryWrapper.eq(EduSubject::getTitle, title);//查看一级分类是否存在
        eduSubjectLambdaQueryWrapper.eq(EduSubject::getParentId, "0");//只查看一级分类
        EduSubject eduSubject = eduSubjectService.getOne(eduSubjectLambdaQueryWrapper);
        return eduSubject;
    }
}

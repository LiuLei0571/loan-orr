package com.load.third.commpent.cdi.cmp;

import com.load.third.commpent.cdi.annimation.ActivityScope;

import dagger.Subcomponent;

/**
 * 用途：
 * 作者：Created by liulei on 2017/12/10.
 * 邮箱：liulei2@aixuedai.com
 */

@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityCompent {
}

package com.sanyou.sentinel.demo;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author yisuan
 * @date 2023/4/22 16:44
 */
public class SentinelSimpleDemo {

    public static void main(String[] args) {
        //加载流控规则
        initFlowRules();

        for (int i = 0; i < 5; i++) {
            Entry entry = null;
            try {
                entry = SphU.entry("sayHello");
                //被保护的逻辑
                System.out.println("访问sayHello资源");
            } catch (BlockException ex) {
                System.out.println("sayHello资源被限流了，可以进行降级处理");
            } finally {
                if (entry != null) {
                    entry.exit();
                }
            }
        }
    }

    private static void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();

        //创建一个流控规则
        FlowRule rule = new FlowRule();
        //对sayHello这个资源限流
        rule.setResource("sayHello");
        //基于qps限流
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        //qps最大为2，超过2就要被限流
        rule.setCount(2);

        rules.add(rule);

        //设置规则
        FlowRuleManager.loadRules(rules);
    }

}

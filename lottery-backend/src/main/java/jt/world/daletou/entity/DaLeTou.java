package jt.world.daletou.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
/**
* <p>
* 大乐透 实体类
* </p>
*
* @author JT
* @since 2025-12-29
*/
@Data
@TableName("da_le_tou")
public class DaLeTou  {
    /**
    * 大乐透期数
    */
    @TableField("id")
    private String id;
    /**
    * 开奖时间
    */
    @TableField("draw_time")
    private LocalDateTime drawTime;
    /**
    * 红色球1号
    */
    @TableField("red_one")
    private String redOne;
    /**
    * 红色球2号
    */
    @TableField("red_two")
    private String redTwo;
    /**
    * 红色球3号
    */
    @TableField("red_three")
    private String redThree;
    /**
    * 红色球4号
    */
    @TableField("red_four")
    private String redFour;
    /**
    * 红色球5号
    */
    @TableField("red_five")
    private String redFive;
    /**
    * 蓝色球1号
    */
    @TableField("blue_one")
    private String blueOne;
    /**
    * 蓝色球2号
    */
    @TableField("blue_two")
    private String blueTwo;

}

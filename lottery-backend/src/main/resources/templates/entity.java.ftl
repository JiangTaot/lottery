package ${package.Entity};

<#if entityLombokModel>
import lombok.Data;
</#if>
<#list table.importPackages as pkg>
    <#if activeRecord>
import ${pkg};
    <#elseIf pkg != "java.io.Serializable">
import ${pkg};
    <#else>
    </#if>
</#list>
<#if superEntityClass??>
import ${package.Entity}.${superEntityClass};
</#if>
import com.baomidou.mybatisplus.annotation.TableField;
<#if activeRecord>
import java.io.Serial;
</#if>
/**
* <p>
* ${table.comment!} 实体类
* </p>
*
* @author ${author}
* @since ${date}
*/
<#if entityLombokModel>
@Data
</#if>
@TableName("${table.name}")
<#if superEntityClass??>
public class ${entity} extends ${superEntityClass}<#if activeRecord> implements Model<${entity}>, Serializable</#if> {
<#else>
public class ${entity}<#if activeRecord> implements Model<${entity}>, Serializable</#if>  {
</#if>
<#if activeRecord>
    @Serial
    private static final long serialVersionUID = 1L;
</#if>
<#-- 生成字段 -->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>
    <#if field.comment!?length gt 0>
    /**
    * ${field.comment}
    */
    </#if>
    <#if field.keyFlag>
    @TableId(value = "${field.columnName}", type = IdType.AUTO)
    <#elseIf field.fill??>
    @TableField(value = "${field.columnName}", fill = FieldFill.${field.fill})
    <#else>
    @TableField("${field.columnName}")
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>

<#if !entityLombokModel>
    // getter/setter 方法将在这里生成
    <#list table.fields as field>

    public ${field.propertyType} get${field.capitalName}() {
        return ${field.propertyName};
    }

    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
        this.${field.propertyName} = ${field.propertyName};
    }
    </#list>
</#if>
}

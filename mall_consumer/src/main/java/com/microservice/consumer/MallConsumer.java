package com.microservice.consumer;

import com.microservice.model.HouseHold;
import com.microservice.model.PCService;
import com.microservice.model.Result;
import com.microservice.service.HouseHoldProvider;
import com.microservice.service.HouseHoldProviderDeta;
import com.microservice.service.PCServiceProvider;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;

//消费者
@RestSchema(schemaId = "consumer")
@Path("/consumer/v0")
public class MallConsumer {

    //rpc远程注入HouseHoldProvider
    @RpcReference(microserviceName = "householdprovider",schemaId = "household")
    private HouseHoldProvider houseHoldProvider;

    //rpc远程注入HouseHoldProviderDeta
    @RpcReference(microserviceName = "householdproviderdeta", schemaId = "household")
    private HouseHoldProviderDeta houseHoldProviderDeta;

    //rpc远程注入PCServiceProvider
    @RpcReference(microserviceName = "pcserviceprovider",schemaId = "pcservice")
    private PCServiceProvider pcServiceProvider;

    //获取配置key
    private DynamicStringProperty sellprefix = DynamicPropertyFactory.getInstance().getStringProperty("household.sellhousehold","",notifyconfigRefreshed());

    private Runnable notifyconfigRefreshed(){
        return ()->LOGGER.info("config[household.sellhousehold] changed to [{}]!",sellprefix.getValue());
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(MallConsumer.class);

    //============v0===============
    /****
     * 家电业务
     * @param name
     * @return
     */
    @Path("/sell/{name}")
    @GET
    public Result sellElec(@PathParam("name")String name)throws Exception{
        return houseHoldProvider.sell(name);
    }

    /****
     * 查看所有
     * @return
     */
    @Path("/list")
    @GET
    public List<HouseHold> list(){
        return houseHoldProvider.list();
    }

    /****
     * 测试配置
     * @param name
     * @return
     */
    @Path("/conf/{name}")
    @GET
    public String getStr(@PathParam("name")String name){
        return sellprefix.getValue()+""+houseHoldProvider.getStr(name);
    }
    //================================v1
    /****
     * 家电业务
     * @param name
     * @return
     */
    @Path("/sellex/{name}")
    @GET
    public Result sellElecEx(@PathParam("name") String name) throws Exception {
        return houseHoldProviderDeta.sell(name);
    }

    /****
     * 查看所有
     * @return
     */
    @Path("/listex")
    @GET
    public List<HouseHold> listEx() {
        return houseHoldProviderDeta.list();
    }

    //=========pcservice v0============
    /****
     * PC业务
     * @param pname
     * @return
     */
    @Path("/sellpc/{pname}")
    @GET
    public PCService sellPc(@PathParam("pname") String pname){
        return pcServiceProvider.sell(pname);
    }
    //所有外设
    @Path("/plist")
    @GET
    public List<PCService> plist(){
        return pcServiceProvider.list();
    }
    //=========pcservice v0============
}

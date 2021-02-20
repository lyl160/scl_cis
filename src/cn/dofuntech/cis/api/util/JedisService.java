package cn.dofuntech.cis.api.util;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.SortingParams;
import redis.clients.util.SafeEncoder;

/**redis工具类
 * @author luokai
 *
 */
public class JedisService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(JedisService.class);
	
	/**
     * 缓存生存时间
     */
    private final int expire = 60000;
    
    /** 操作Key的方法 */
    public Keys KEYS =new Keys();
    /** 对存储结构为String类型的操作 */
    public Strings STRINGS =new Strings();
    /** 对存储结构为List类型的操作 */
    public Lists LISTS=new Lists();
    /** 对存储结构为Set类型的操作 
    public Sets SETS;*/
    /** 对存储结构为HashMap类型的操作 
    public Hash HASH;*/
    /** 对存储结构为Set(排序的)类型的操作
    public SortSet SORTSET; */
    
    private JedisPool jedisPool;

	/**
	 * @param jedisPool the jedisPool to set
	 */
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public JedisPool getPool() {
        return jedisPool;
    }
 
    /**
     * 从jedis连接池中获取获取jedis对象
     * 
     * @return
     */
    public Jedis getJedis() {
        return jedisPool.getResource();
    }
 
    /**
     * 回收jedis
     * 
     * @param jedis
     */
    public void returnJedis(Jedis jedis) {
    	if(null != jedis){
            jedisPool.returnResource(jedis);
    	}
    }
    
    
    /**
     * 设置过期时间
     * 
     * @author ruan 2013-4-11
     * @param key
     * @param seconds
     */
    public void expire(String key, int seconds) {
        if (seconds <= 0) {
            return;
        }
        Jedis jedis = null;
        try{
            jedis = getJedis();
            jedis.expire(key, seconds);
        }catch(Exception ex){
        	LOGGER.error("过期缓存发生错误:"+ex.getMessage());
        }finally {
            returnJedis(jedis);
		}
    }
 
    /**
     * 设置默认过期时间
     * 
     * @author ruan 2013-4-11
     * @param key
     */
    public void expire(String key) {
        expire(key, expire);
    }
    
    public class Keys{
    	/**
         * 清空所有key
         */
        public String flushAll() {
        	String stata=null;
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                stata = jedis.flushAll();
            }catch(Exception ex){
            	LOGGER.error("清空全部缓存key发生错误:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return stata;
        }
        /**
         * 更改key 
         * @param String oldkey
         * @param String newkey
         * @return 状态码
         * */
        public String rename(String oldkey, String newkey) {
            return rename(SafeEncoder.encode(oldkey), SafeEncoder.encode(newkey));
        }
 
        /**
         * 更改key,仅当新key不存在时才执行 
         * @param String oldkey
         * @param String newkey
         * @return 状态码
         * */
        public long renamenx(String oldkey, String newkey) {
        	long status =0;
            Jedis jedis = null;
            try{
                jedis = getJedis();
                status = jedis.renamenx(oldkey, newkey);
            }catch(Exception ex){
            	LOGGER.error("更新key[renamenx]字段:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return status;
        }
 
        /**
         * 更改key
         * 
         * @param String oldkey
         * @param String newkey
         * @return 状态码
         * */
        public String rename(byte[] oldkey, byte[] newkey) {
        	String status =null;
            Jedis jedis = null;
            try{
                jedis = getJedis();
                status = jedis.rename(oldkey, newkey);
            }catch(Exception ex){
            	LOGGER.error("更新key[rename]字段:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return status;
        }
 
        /**
         * 设置key的过期时间，以秒为单位
         * @param String  key
         * @param 时间  ,已秒为单位
         * @return 影响的记录数
         * */
        public long expired(String key, int seconds) {
        	long status =0;
            Jedis jedis = null;
            try{
                jedis = getJedis();
                status = jedis.expire(key, seconds);
            }catch(Exception ex){
            	LOGGER.error("设置key过期时间:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return status;
        }
 
        /**
         * 设置key的过期时间,它是距历元（即格林威治标准时间 1970 年 1 月 1 日的 00:00:00，格里高利历）的偏移量。
         * @param String  key
         * @param 时间 ,已秒为单位
         * @return 影响的记录数
         * */
        public long expireAt(String key, long timestamp) {
            long status =0;
            Jedis jedis = null;
            try{
                jedis = getJedis();
                status =  jedis.expireAt(key, timestamp);
            }catch(Exception ex){
            	LOGGER.error("设置key过期时间:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return status;
        }
 
        /**
         * 查询key的过期时间
         * @param String key
         * @return 以秒为单位的时间表示
         * */
        public long ttl(String key) {
        	 long len =0;
             Jedis jedis = null;
             try{
                 jedis = getJedis();
                 len =  jedis.ttl(key);
             }catch(Exception ex){
             	LOGGER.error("查询key的过期时间:"+ex.getMessage());
             }finally {
                 returnJedis(jedis);
     		}
             return len;
        }
 
        /**
         * 取消对key过期时间的设置
         * @param key
         * @return 影响的记录数
         * */
        public long persist(String key) {
        	long count =0;
            Jedis jedis = null;
            try{
                jedis = getJedis();
                count =  jedis.persist(key);
            }catch(Exception ex){
            	LOGGER.error("取消对key过期时间的设置:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return count;
        }
 
        /**
         * 删除keys对应的记录,可以是多个key
         * @param String  ... keys
         * @return 删除的记录数
         * */
        public long del(String... keys) {
        	long count =0;
            Jedis jedis = null;
            try{
                jedis = getJedis();
                count =  jedis.del(keys);
            }catch(Exception ex){
            	LOGGER.error("删除keys对应的记录:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return count;
        }
 
        /**
         * 删除keys对应的记录,可以是多个key
         * 
         * @param String  ... keys
         * @return 删除的记录数
         * */
        public long del(byte[]... keys) {
        	long count =0;
            Jedis jedis = null;
            try{
                jedis = getJedis();
                count =  jedis.del(keys);
            }catch(Exception ex){
            	LOGGER.error("删除keys对应的记录:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return count;
        }
 
        /**
         * 判断key是否存在
         * @param String       key
         * @return boolean
         * */
        public boolean exists(String key) {
        	 boolean exis =false;
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                exis =  jedis.exists(key);
            }catch(Exception ex){
            	LOGGER.error("删除keys对应的记录:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return exis;
        }
 
        /**
         * 对List,Set,SortSet进行排序,如果集合数据较大应避免使用这个方法
         * 
         * @param String  key
         * @return List<String> 集合的全部记录
         * **/
        public List<String> sort(String key) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return  jedis.sort(key);
            }catch(Exception ex){
            	LOGGER.error("删除keys对应的记录:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return null;
        }
 
        /**
         * 对List,Set,SortSet进行排序或limit
         * 
         * @param String key
         * @param SortingParams parame 定义排序类型或limit的起止位置.
         * @return List<String> 全部或部分记录
         * **/
        public List<String> sort(String key, SortingParams parame) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return  jedis.sort(key,parame);
            }catch(Exception ex){
            	LOGGER.error("删除keys对应的记录:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return null;
        }
 
        /**
         * 返回指定key存储的类型
         * 
         * @param String key
         * @return String string|list|set|zset|hash
         * **/
        public String type(String key) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return  jedis.type(key);
            }catch(Exception ex){
            	LOGGER.error("返回指定key存储的类型:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return null;
        }
 
        /**
         * 查找所有匹配给定的模式的键
         * @param String  key的表达式,*表示多个，？表示一个
         * */
        public Set<String> keys(String pattern) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return  jedis.keys(pattern);
            }catch(Exception ex){
            	LOGGER.error("查找所有匹配给定的模式的键:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return null;
        }
        
    }
    
 // *******************************************Strings*******************************************//
    public class Strings {
        /**
         * 根据key获取记录
         * @param String key
         * @return 值
         * */
        public String get(String key) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return  jedis.get(key);
            }catch(Exception ex){
            	LOGGER.error("根据key获取数据信息:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return null;
        }
 
        /**
         * 根据key获取记录
         * 
         * @param byte[] key
         * @return 值
         * */
        public byte[] get(byte[] key) {
            Jedis jedis = null;
            try{
                jedis = getJedis();
                return  jedis.get(key);
            }catch(Exception ex){
            	LOGGER.error("根据key获取数据信息:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return null;
        }
 
        /**
         * 添加有过期时间的记录
         * 
         * @param String  key
         * @param int seconds 过期时间，以秒为单位
         * @param String value
         * @return String 操作状态
         * */
        public String setEx(String key, int seconds, String value) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return  jedis.setex(key, seconds, value);
            }catch(Exception ex){
            	LOGGER.error("添加有过期时间的记录:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return null;
        }
 
        /**
         * 添加有过期时间的记录
         * 
         * @param String  key
         * @param int seconds 过期时间，以秒为单位
         * @param String value
         * @return String 操作状态
         * */
        public String setEx(byte[] key, int seconds, byte[] value) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return  jedis.setex(key, seconds, value);
            }catch(Exception ex){
            	LOGGER.error("添加有过期时间的记录:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return null;
        }
 
        /**
         * 添加一条记录，仅当给定的key不存在时才插入
         * 
         * @param String  key
         * @param String  value
         * @return long 状态码，1插入成功且key不存在，0未插入，key存在
         * */
        public long setnx(String key, String value) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return  jedis.setnx(key, value);
            }catch(Exception ex){
            	LOGGER.error("增加一个信息，仅当key存在时候:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return -1;
        }
 
        /**
         * 添加记录,如果记录已存在将覆盖原有的value
         * 
         * @param String
         *            key
         * @param String
         *            value
         * @return 状态码
         * */
        public String set(String key, String value) {
            return set(SafeEncoder.encode(key), SafeEncoder.encode(value));
        }
 
        /**
         * 添加记录,如果记录已存在将覆盖原有的value
         * 
         * @param String
         *            key
         * @param String
         *            value
         * @return 状态码
         * */
        public String set(String key, byte[] value) {
            return set(SafeEncoder.encode(key), value);
        }
 
        /**
         * 添加记录,如果记录已存在将覆盖原有的value
         * 
         * @param byte[] key
         * @param byte[] value
         * @return 状态码
         * */
        public String set(byte[] key, byte[] value) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return  jedis.set(key, value);
            }catch(Exception ex){
            	LOGGER.error("添加记录,如果记录已存在将覆盖原有的value:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return null;
        }
 
        /**
         * 从指定位置开始插入数据，插入的数据会覆盖指定位置以后的数据<br/>
         * 例:String str1="123456789";<br/>
         * 对str1操作后setRange(key,4,0000)，str1="123400009";
         * 
         * @param String
         *            key
         * @param long offset
         * @param String
         *            value
         * @return long value的长度
         * */
        public long setRange(String key, long offset, String value) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return  jedis.setrange(key, offset, value);
            }catch(Exception ex){
            	LOGGER.error("添加记录,如果记录已存在将覆盖原有的value:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return -1;
            
        }
 
        /**
         * 在指定的key中追加value
         * 
         * @param String
         *            key
         * @param String
         *            value
         * @return long 追加后value的长度
         * **/
        public long append(String key, String value) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return  jedis.append(key, value);
            }catch(Exception ex){
            	LOGGER.error("添加记录,如果记录已存在将覆盖原有的value:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return -1;
        }
 
        /**
         * 将key对应的value减去指定的值，只有value可以转为数字时该方法才可用
         * 
         * @param String
         *            key
         * @param long number 要减去的值
         * @return long 减指定值后的值
         * */
        public long decrBy(String key, long number) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return  jedis.decrBy(key, number);
            }catch(Exception ex){
            	LOGGER.error("将key对应的value减去指定的值，只有value可以转为数字时该方法才可用:"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return -1;
        }
 
        /**
         * <b>可以作为获取唯一id的方法</b><br/>
         * 将key对应的value加上指定的值，只有value可以转为数字时该方法才可用
         * 
         * @param String
         *            key
         * @param long number 要减去的值
         * @return long 相加后的值
         * */
        public long incrBy(String key, long number) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return  jedis.incrBy(key, number);
            }catch(Exception ex){
            	LOGGER.error("将key对应的value加上指定的值，只有value可以转为数字时该方法才可用"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return -1;
        }
 
        /**
         * 对指定key对应的value进行截取
         * 
         * @param String
         *            key
         * @param long startOffset 开始位置(包含)
         * @param long endOffset 结束位置(包含)
         * @return String 截取的值
         * */
        public String getrange(String key, long startOffset, long endOffset) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return  jedis.getrange(key, startOffset, endOffset);
            }catch(Exception ex){
            	LOGGER.error("对指定key对应的value进行截取"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return null;
        }
        /**
         * 获取并设置指定key对应的value<br/>
         * 如果key存在返回之前的value,否则返回null
         * 
         * @param String
         *            key
         * @param String
         *            value
         * @return String 原始value或null
         * */
        public String getSet(String key, String value) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return  jedis.getSet(key, value);
            }catch(Exception ex){
            	LOGGER.error("如果key存在返回之前的value,否则返回null"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return null;
        }
 
        /**
         * 批量获取记录,如果指定的key不存在返回List的对应位置将是null
         * 
         * @param String
         *            keys
         * @return List<String> 值得集合
         * */
        public List<String> mget(String... keys) {
        	
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return jedis.mget(keys);
            }catch(Exception ex){
            	LOGGER.error("批量获取记录,如果指定的key不存在返回List的对应位置将是null"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return null;
        }
 
        /**
         * 批量存储记录
         * 
         * @param String
         *            keysvalues 例:keysvalues="key1","value1","key2","value2";
         * @return String 状态码
         * */
        public String mset(String... keysvalues) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return jedis.mset(keysvalues);
            }catch(Exception ex){
            	LOGGER.error("批量存储记录"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return null;
        }
 
        /**
         * 获取key对应的值的长度
         * 
         * @param String
         *            key
         * @return value值得长度
         * */
        public long strlen(String key) {
        	Jedis jedis = null;
            try{
                jedis = getJedis();
                return jedis.strlen(key);
            }catch(Exception ex){
            	LOGGER.error("获取key对应的值的长度"+ex.getMessage());
            }finally {
                returnJedis(jedis);
    		}
            return -1;
        }
    }
    
// *******************************************Lists*******************************************//
   public class Lists {
       /**
        * List长度
        * @param String   key
        * @return 长度
        * */
       public long llen(String key) {
           return llen(SafeEncoder.encode(key));
       }

       /**
        * List长度
        * @param byte[] key
        * @return 长度
        * */
       public long llen(byte[] key) {          
           Jedis jedis = null;
           try{
               jedis = getJedis();
               return  jedis.llen(key);
           }catch(Exception ex){
           	LOGGER.error("获取队列中的长度:"+ex.getMessage());
           }finally {
               returnJedis(jedis);
   		   }
           return -1;
       }

       /**
        * 覆盖操作,将覆盖List中指定位置的值
        * 
        * @param byte[] key
        * @param int index 位置
        * @param byte[] value 值
        * @return 状态码
        * */
       public String lset(byte[] key, int index, byte[] value) {
    	   Jedis jedis = null;
           try{
               jedis = getJedis();
               return  jedis.lset(key, index, value);
           }catch(Exception ex){
           	LOGGER.error("覆盖操作,将覆盖List中指定位置的值:"+ex.getMessage());
           }finally {
               returnJedis(jedis);
   		   }
           return null;
       }

       /**
        * 覆盖操作,将覆盖List中指定位置的值
        * @param key
        * @param int index 位置
        * @param String  value 值
        * @return 状态码
        * */
       public String lset(String key, int index, String value) {
           return lset(SafeEncoder.encode(key), index, SafeEncoder.encode(value));
       }

       /**
        * 在value的相对位置插入记录
        * @param key
        * @param LIST_POSITION  前面插入或后面插入
        * @param String  pivot 相对位置的内容
        * @param String value 插入的内容
        * @return 记录总数
        * */
       public long linsert(String key, LIST_POSITION where, String pivot, String value) {
           return linsert(SafeEncoder.encode(key), where, SafeEncoder.encode(pivot), SafeEncoder.encode(value));
       }

       /**
        * 在指定位置插入记录
        * 
        * @param String  key
        * @param LIST_POSITION  前面插入或后面插入
        * @param byte[] pivot 相对位置的内容
        * @param byte[] value 插入的内容
        * @return 记录总数
        * */
       public long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
    	   Jedis jedis = null;
           try{
               jedis = getJedis();
               return  jedis.linsert(key, where, pivot, value);
           }catch(Exception ex){
           	LOGGER.error("覆盖操作,在指定位置插入记录:"+ex.getMessage());
           }finally {
               returnJedis(jedis);
   		   }
           return -1;
       }

       /**
        * 获取List中指定位置的值
        * 
        * @param String   key
        * @param int index 位置
        * @return 值
        * **/
       public String lindex(String key, int index) {
           return SafeEncoder.encode(lindex(SafeEncoder.encode(key), index));
       }

       /**
        * 获取List中指定位置的值
        * 
        * @param byte[] key
        * @param int index 位置
        * @return 值
        * **/
       public byte[] lindex(byte[] key, int index) {
           Jedis jedis = null;
           try{
               jedis = getJedis();
               return jedis.lindex(key, index);
           }catch(Exception ex){
           	LOGGER.error("获取List中指定位置的值:"+ex.getMessage());
           }finally {
               returnJedis(jedis);
   		   }
           return null;
       }

       /**
        * 将List中的第一条记录移出List
        * 
        * @param String key
        * @return 移出的记录
        * */
       public String lpop(String key) {
           return SafeEncoder.encode(lpop(SafeEncoder.encode(key)));
       }

       /**
        * 将List中的第一条记录移出List
        * 
        * @param byte[] key
        * @return 移出的记录
        * */
       public byte[] lpop(byte[] key) {
    	   Jedis jedis = null;
           try{
               jedis = getJedis();
               return jedis.lpop(key);
           }catch(Exception ex){
           	LOGGER.error("将List中的第一条记录移出List:"+ex.getMessage());
           }finally {
               returnJedis(jedis);
   		   }
           return null;
       }

       /**
        * 将List中最后第一条记录移出List
        * 
        * @param byte[] key
        * @return 移出的记录
        * */
       public String rpop(String key) {
    	   Jedis jedis = null;
           try{
               jedis = getJedis();
               return jedis.rpop(key);
           }catch(Exception ex){
           	LOGGER.error("将List中最后第一条记录移出List:"+ex.getMessage());
           }finally {
               returnJedis(jedis);
   		   }
           return null;
       }

       /**
        * 向List尾部追加记录
        * 
        * @param String  key
        * @param String  value
        * @return 记录总数
        * */
       public long lpush(String key, String value) {
           return lpush(SafeEncoder.encode(key), SafeEncoder.encode(value));
       }

       /**
        * 向List头部追加记录
        * 
        * @param String key
        * @param String value
        * @return 记录总数
        * */
       public long rpush(String key, String value) {
    	   Jedis jedis = null;
           try{
               jedis = getJedis();
               return jedis.rpush(key,value);
           }catch(Exception ex){
           	LOGGER.error("向List头部追加记录:"+ex.getMessage());
           }finally {
               returnJedis(jedis);
   		   }
           return -1;
       }

       /**
        * 向List头部追加记录
        * 
        * @param String  key
        * @param String  value
        * @return 记录总数
        * */
       public long rpush(byte[] key, byte[] value) {
    	   Jedis jedis = null;
           try{
               jedis = getJedis();
               return jedis.rpush(key,value);
           }catch(Exception ex){
           	LOGGER.error("向List头部追加记录:"+ex.getMessage());
           }finally {
               returnJedis(jedis);
   		   }
           return -1;
       }

       /**
        * 向List中追加记录
        * 
        * @param byte[] key
        * @param byte[] value
        * @return 记录总数
        * */
       public long lpush(byte[] key, byte[] value) {
    	   Jedis jedis = null;
           try{
               jedis = getJedis();
               return jedis.lpush(key, value);
           }catch(Exception ex){
           	LOGGER.error("向List中追加记录:"+ex.getMessage(),ex);
           }finally {
               returnJedis(jedis);
   		   }
           return -1;
       }

       /**
        * 获取指定范围的记录，可以做为分页使用
        * 
        * @param String  key
        * @param long start
        * @param long end
        * @return List
        * */
       public List<String> lrange(String key, long start, long end) {
    	   
    	   Jedis jedis = null;
           try{
               jedis = getJedis();
               return jedis.lrange(key, start, end);
           }catch(Exception ex){
           	LOGGER.error("获取指定范围的记录，可以做为分页使用:"+ex.getMessage());
           }finally {
               returnJedis(jedis);
   		   }
           return null;
       }

       /**
        * 获取指定范围的记录，可以做为分页使用
        * 
        * @param byte[] key
        * @param int start
        * @param int end 如果为负数，则尾部开始计算
        * @return List
        * */
       public List<byte[]> lrange(byte[] key, int start, int end) {
    	   Jedis jedis = null;
           try{
               jedis = getJedis();
               return jedis.lrange(key, start, end);
           }catch(Exception ex){
           	LOGGER.error("获取指定范围的记录，可以做为分页使用:"+ex.getMessage());
           }finally {
               returnJedis(jedis);
   		   }
           return null;
       }

       /**
        * 删除List中c条记录，被删除的记录值为value
        * 
        * @param byte[] key
        * @param int c 要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
        * @param byte[] value 要匹配的值
        * @return 删除后的List中的记录数
        * */
       public long lrem(byte[] key, int c, byte[] value) {
    	   
    	   Jedis jedis = null;
           try{
               jedis = getJedis();
               return jedis.lrem(key, c, value);
           }catch(Exception ex){
           	LOGGER.error("删除List中c条记录，被删除的记录值为value:"+ex.getMessage());
           }finally {
               returnJedis(jedis);
   		   }
           return -1;
       }

       /**
        * 删除List中c条记录，被删除的记录值为value
        * 
        * @param String key
        * @param int c 要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
        * @param String   value 要匹配的值
        * @return 删除后的List中的记录数
        * */
       public long lrem(String key, int c, String value) {
           return lrem(SafeEncoder.encode(key), c, SafeEncoder.encode(value));
       }

       /**
        * 算是删除吧，只保留start与end之间的记录
        * 
        * @param byte[] key
        * @param int start 记录的开始位置(0表示第一条记录)
        * @param int end 记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
        * @return 执行状态码
        * */
       public String ltrim(byte[] key, int start, int end) {
    	   
    	   Jedis jedis = null;
           try{
               jedis = getJedis();
               return jedis.ltrim(key, start, end);
           }catch(Exception ex){
           	LOGGER.error("算是删除吧，只保留start与end之间的记录："+ex.getMessage());
           }finally {
               returnJedis(jedis);
   		   }
           return null;
       }

       /**
        * 算是删除吧，只保留start与end之间的记录
        * 
        * @param String
        *            key
        * @param int start 记录的开始位置(0表示第一条记录)
        * @param int end 记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
        * @return 执行状态码
        * */
       public String ltrim(String key, int start, int end) {
           return ltrim(SafeEncoder.encode(key), start, end);
       }
   }
}

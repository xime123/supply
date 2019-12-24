# SOSO 合约API

[![Documentation chat](https://img.shields.io/badge/gitter-Docs%20chat-4AB495.svg)](#)

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

**Contents**

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## 通用定义

### 错误码

    enum Code { FAIL, SUCCESS }
    
> 在返回时，`FAIL` 用 `0` 表示，`SUCCESS` 用 `1` 表示。

### 事件

**Response**

    event Response(address indexed from, uint256 errno, string indexed errmsg);


字段 | 描述 | 示例
---|---|---
from | 当前交易的发起者 | msg.sender
error | 交易错误码，0 失败 1 成功 | 1
errmsg | 对应错误码的描述信息 | success

### 产品周期

    enum Period { PREPARE, PRODUCT, TRANSPORT, SALE }
    
参数| 描述 | 代表值
---|---|---
PREPARE | 原材料阶段 | 0
PRODUCT | 生产阶段 |  1
TRANSPORT | 运输阶段 |  2
SALE | 销售阶段 |  3


## 批次合约

### 功能描述

该合约(`Category.sol`)主要负责对某类产品的某个批次的基本信息进行维护，主要包括：

* 对原材料、制作工艺等基本信息的更新。
* 查询该批次的基本信息

### API-初始化函数

#### 接口定义

    function Category(string _name) public{}

#### 接口描述

初始化批次信息，仅执行一次。
	
#### 权限说明

* 合约发布时仅可调用一次；
    
#### 请求参数

参数 | 描述 | 示例值
---|---|---
_name | 批次产品名称 | 顶级香烟

#### 响应事件

无


### API-基本信息更新

#### 接口定义

    function updateBaseInfo(string _technology, string _material) public ownerOnly
	
#### 接口描述

更新产品基本信息，批次信息，产品工艺
	
#### 权限说明

* 暂仅支持合约所有者才可进行调用；
* 不支持调用合约是传递金额；
    
#### 请求参数

参数 | 描述 | 示例值
---|---|---
_technology | 技术工艺 | 制麦、糖化、发酵、包装
_material | 原材料信息 | 大麦、酒花、淀粉及辅助材料

#### 响应事件

Response


### API-查询原材料信息

#### 接口定义

    function getMaterial() public constant returns(string _material) {}
	
#### 接口描述

获取当前批次的原材料信息
	
#### 权限说明

* 调用不受限制，为查询调用；
    
#### 请求参数

无

#### 响应参数

 参数 | 描述 | 示例值
---|---|---
 _material | 原材料信息 | 大麦、酒花、淀粉及辅助材料


### API-查询工艺信息

#### 接口定义

    function getTechnology() public constant returns(string _technology) {}
	
#### 接口描述

获取当前批次的工艺信息
	
#### 权限说明

* 调用不受限制，为查询调用；
    
#### 请求参数

无

#### 响应参数

 参数 | 描述 | 示例值
---|---|---
 _technology | 工艺信息 | 制麦、糖化、发酵、包装


## 产品管理合约

### 功能描述

该合约(`ProductManager.sol`)主要负责生产某个批次的产品，主要包括：

* 持有代币合约地址；
* 根据批次信息创建一个新的具体产品（`Product.sol`）合约对象；
* 创建产品时如果要给新产品分配代币，则需要保证管理合约本身拥有该代币；

### API-初始化函数

#### 接口定义

    function ProductManager (address _token) public {}

#### 接口描述

初始化管理合约信息，接收参数为代币合约地址，后期可调用接口进行更改。
	
#### 权限说明

* 合约发布时仅可调用一次；
    
#### 请求参数

参数 | 描述 | 示例值
---|---|---
_token | 代币合约地址 | 0x...

#### 响应事件

无
 
### API-生产产品

#### 接口定义

    function createProduct (address _category, address _authorized, address _tokenOwner, uint256 _value) 
		public 
		ownerOnly
	{}
	
#### 接口描述

该接口会创建一个新的产品（发布新的合约），同时会通过事件机制返回合约的地址。
	
#### 权限说明

* 暂仅支持合约所有者才可进行调用；
    
#### 请求参数

参数 | 描述 | 示例值
---|---|---
_category | 产品批次合约地址（批次号） | 0x...
_authorized | 鉴权合约地址 | 0x...
_tokenOwner | 该新产品隐藏的代币最终所有者 | 0x...
_value | 该新产品隐藏的代币数 | 100000

#### 响应事件

    event Create(address indexed from, uint256 errno, address indexed _addr);

字段 | 描述 | 示例
---|---|---
from | 当前交易的发起者 | msg.sender
errno | 交易错误码，0 失败 1 成功 | 1
_addr | 新创建的合约地址 | 0x...


## 产品合约

### 功能描述

该合约(`Product.sol`)对应每个具体的产品，一个合约对应一个产品，一个合约记录该产品的整个生命周期过程，主要包括：

* 维护产品各个声明周期的数据信息；
* 对操作者进行鉴权；
* 代币最终所有者可以提取代币；

### API-初始化函数

#### 接口定义

    function Product (address _category, address _token, address _authorized, address _tokenOwner) {}

#### 接口描述

初始化产品合约，需要批次信息，代币信息，鉴权信息，代币所有者信息。
	
#### 权限说明

* 合约发布时仅可调用一次；
    
#### 请求参数

参数 | 描述 | 示例值
---|---|---
_category | 批次信息 | 0x...
_token | 代币合约地址 | 0x...
_authorized | 鉴权地址信息 | 0x...
_tokenOwner | 合约所有者地址，只有该地址才能提取隐藏在产品中的代币 | 0x...

#### 响应事件

无

### API-提取代币

#### 接口定义

	function withdrawERC20Token() public onlyTokenOwner {}

#### 接口描述

隐藏在产品中的代币可以被提取。
	
#### 权限说明

* 仅能使用代币最终所有者的地址才能提取。
    
#### 请求参数

无

#### 响应事件

Response


### API-更新产品状态

#### 接口定义

    function appendEntry(uint _peroid, string _detailInfo) public onlyAuthorized(authorizedAddr) {}

#### 接口描述

更新产品生命周期信息。
	
#### 权限说明

* 仅在鉴权合约中授权过的地址才能进行调用。
    
#### 请求参数

参数 | 描述 | 示例值
---|---|---
_peroid | 产品生命周期 | 0 原材料阶段, 1 生产阶段, 2 运输阶段, 3 销售阶段
_detailInfo | 当前声明周期溯源信息 | {"peroid":0,"op":"小三","un":"美食公司","ua":"天堂路"}

#### 响应事件

Response 

### API-查询产品溯源信息

#### 接口定义

    function retriveAllEntry () public constant returns(string) {}

#### 接口描述

查询产品各个生命周期的数据，返回按顺序返回。
	
#### 权限说明

* 仅在鉴权合约中授权过的地址才能进行调用。
    
#### 请求参数

无

#### 响应参数

参数 | 描述 | 示例值
---|---|---
_info | 产品溯源信息 | {"peroid":0,"op":"小三","un":"美食公司","ua":"天堂路"}${"peroid":1,"op":"小三","un":"美食公司","ua":"天堂路"}








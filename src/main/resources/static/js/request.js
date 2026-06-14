/**
 * 封装异步请求工具
 * 基于 Axios 实现，提供统一的请求配置和响应处理
 */

class Request {
    constructor() {
        // 基础配置
        this.baseURL = '/api';
        this.timeout = 10000;
        
        // 请求拦截器
        axios.interceptors.request.use(
            config => this._requestInterceptor(config),
            error => Promise.reject(error)
        );
        
        // 响应拦截器
        axios.interceptors.response.use(
            response => this._responseInterceptor(response),
            error => this._errorHandler(error)
        );
    }

    /**
     * 请求拦截器 - 统一处理请求配置
     */
    _requestInterceptor(config) {
        // 设置基础URL
        if (!config.url.startsWith('http')) {
            config.url = this.baseURL + config.url;
        }
        
        // 添加Token
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = 'Bearer ' + token;
        }
        
        // 设置默认Content-Type
        if (!config.headers['Content-Type']) {
            config.headers['Content-Type'] = 'application/json';
        }
        
        return config;
    }

    /**
     * 响应拦截器 - 统一处理响应数据
     */
    _responseInterceptor(response) {
        const { data } = response;
        
        // 统一处理业务码
        if (data.code === 200) {
            return data;
        } else {
            // 业务错误，抛出异常
            const error = new Error(data.message || '请求失败');
            error.code = data.code;
            error.data = data;
            return Promise.reject(error);
        }
    }

    /**
     * 错误处理器
     */
    _errorHandler(error) {
        if (error.response) {
            const { status } = error.response;
            
            // 401 未授权
            if (status === 401) {
                localStorage.removeItem('token');
                localStorage.removeItem('username');
                localStorage.removeItem('userId');
                window.location.reload();
                return Promise.reject(new Error('登录已过期，请重新登录'));
            }
            
            // 其他HTTP错误
            const message = error.response.data?.message || `请求失败 [${status}]`;
            return Promise.reject(new Error(message));
        } else if (error.request) {
            return Promise.reject(new Error('网络请求超时'));
        } else {
            return Promise.reject(error);
        }
    }

    /**
     * GET 请求
     * @param {string} url - 请求地址
     * @param {object} params - 请求参数
     * @returns {Promise}
     */
    get(url, params = {}) {
        return axios.get(url, { params });
    }

    /**
     * POST 请求
     * @param {string} url - 请求地址
     * @param {object} data - 请求体
     * @returns {Promise}
     */
    post(url, data = {}) {
        return axios.post(url, data);
    }

    /**
     * PUT 请求
     * @param {string} url - 请求地址
     * @param {object} data - 请求体
     * @returns {Promise}
     */
    put(url, data = {}) {
        return axios.put(url, data);
    }

    /**
     * DELETE 请求
     * @param {string} url - 请求地址
     * @param {object} params - 请求参数
     * @returns {Promise}
     */
    delete(url, params = {}) {
        return axios.delete(url, { params });
    }
}

// 创建全局请求实例
const request = new Request();

// Mock数据（当后端接口不可用时使用）
const MockData = {
    categories: [
        { id: 0, name: '全部' },
        { id: 1, name: '手机数码' },
        { id: 2, name: '电脑办公' },
        { id: 3, name: '智能穿戴' },
        { id: 4, name: '平板设备' }
    ],
    
    products: [
        { productId: 1, name: 'iPhone 15 Pro', price: 8999, stock: 50, categoryId: 1, image: '📱', promotionType: 'limited', discountRate: 0.85 },
        { productId: 2, name: 'iPhone 15', price: 6999, stock: 100, categoryId: 1, image: '📱', promotionType: 'discount', discountRate: 0.9 },
        { productId: 3, name: 'MacBook Pro 14', price: 14999, stock: 30, categoryId: 2, image: '💻', promotionType: 'new', discountRate: 1 },
        { productId: 4, name: 'MacBook Air', price: 9499, stock: 60, categoryId: 2, image: '💻', promotionType: 'hot', discountRate: 0.95 },
        { productId: 5, name: 'AirPods Pro 2', price: 1899, stock: 200, categoryId: 3, image: '🎧', promotionType: 'limited', discountRate: 0.8 },
        { productId: 6, name: 'Apple Watch Ultra', price: 6499, stock: 25, categoryId: 3, image: '⌚', promotionType: 'none', discountRate: 1 },
        { productId: 7, name: 'iPad Pro 12.9', price: 8499, stock: 40, categoryId: 4, image: '📲', promotionType: 'discount', discountRate: 0.88 },
        { productId: 8, name: 'iPad Air', price: 4999, stock: 80, categoryId: 4, image: '📲', promotionType: 'new', discountRate: 1 },
        { productId: 9, name: '华为 Mate 60 Pro', price: 6999, stock: 70, categoryId: 1, image: '📱', promotionType: 'hot', discountRate: 0.92 },
        { productId: 10, name: '小米 14 Ultra', price: 5999, stock: 90, categoryId: 1, image: '📱', promotionType: 'limited', discountRate: 0.85 },
        { productId: 11, name: 'ThinkPad X1', price: 12999, stock: 35, categoryId: 2, image: '💻', promotionType: 'none', discountRate: 1 },
        { productId: 12, name: '华为 Watch 4', price: 2999, stock: 100, categoryId: 3, image: '⌚', promotionType: 'discount', discountRate: 0.9 }
    ]
};

/**
 * Mock请求服务
 */
class MockService {
    constructor() {
        this.products = MockData.products;
        this.categories = MockData.categories;
    }

    /**
     * 获取分类列表
     */
    getCategories() {
        return Promise.resolve({
            code: 200,
            message: 'success',
            data: this.categories
        });
    }

    /**
     * 分页查询商品列表
     * @param {number} page - 页码
     * @param {number} size - 每页数量
     * @param {string} keyword - 搜索关键词
     * @param {number} categoryId - 分类ID
     */
    getProducts(page = 1, size = 10, keyword = '', categoryId = 0) {
        let filtered = [...this.products];
        
        // 分类筛选
        if (categoryId > 0) {
            filtered = filtered.filter(p => p.categoryId === categoryId);
        }
        
        // 关键词搜索
        if (keyword) {
            filtered = filtered.filter(p => 
                p.name.toLowerCase().includes(keyword.toLowerCase())
            );
        }
        
        // 分页
        const total = filtered.length;
        const start = (page - 1) * size;
        const end = start + size;
        const records = filtered.slice(start, end);
        
        return Promise.resolve({
            code: 200,
            message: 'success',
            data: {
                records,
                total,
                size,
                current: page,
                pages: Math.ceil(total / size)
            }
        });
    }

    /**
     * 获取商品详情
     * @param {number} productId - 商品ID
     */
    getProductById(productId) {
        const product = this.products.find(p => p.productId === productId);
        if (product) {
            return Promise.resolve({
                code: 200,
                message: 'success',
                data: product
            });
        }
        return Promise.resolve({
            code: 404,
            message: '商品不存在',
            data: null
        });
    }
}

// 创建Mock服务实例
const mockService = new MockService();

// 导出
window.Request = Request;
window.request = request;
window.MockData = MockData;
window.MockService = MockService;
window.mockService = mockService;
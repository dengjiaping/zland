package com.zhisland.android.blog.common.util;

import android.content.res.AssetManager;

import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.common.dto.City;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.profile.dto.CompanyState;
import com.zhisland.android.blog.profile.dto.CompanyType;
import com.zhisland.lib.component.application.ZHApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 从assert中预加载一些本地数据，用于保证在没有网或者数据接口拉取失败时，默认显示的数据
 */
public class LoadDataFromAssert {
    private static final String PATH_CITIES = "city.txt";
    private static final String PATH_COUNTRY = "country.txt";

    private static final String PATH_TYPE_INDUSTRY_PERSONAL = "industry_user.txt";
    private static final String PATH_TYPE_INDUSTRY_COMPANY = "industry_company.txt";

    private static final String PATH_SUPPLY_CATEGORY = "category_supply_user.txt";
    private static final String PATH_DEMAND_CATEGORY = "category_demand_user.txt";

    private static final String PATH_COMPANY_STATE = "company_state.txt";
    private static final String PATH_COMPANY_TYPE = "company_type.txt";

    private static final String PATH_EVENT_ORDER = "event_order.txt";
    private static final String PATH_EVENT_TAG = "event_tag.txt";

    private static LoadDataFromAssert instance;
    private static final Object synObj = new Object();

    private LoadDataFromAssert() {
    }

    public static LoadDataFromAssert getInstance() {
        if (instance == null) {
            synchronized (synObj) {
                instance = new LoadDataFromAssert();
            }
        }
        return instance;
    }

    /**
     * 获取城市列表
     */
    public ArrayList<City> getCities() {
        ArrayList<City> cities = new ArrayList<>();

        AssetManager am = ZHApplication.APP_CONTEXT.getAssets();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    am.open(PATH_CITIES), "UTF-8"));

            int code;
            String name;
            int parentCode;
            String[] tmp;
            City city;
            String mLine = reader.readLine();
            while (mLine != null) {
                tmp = mLine.split("\t");
                code = Integer.valueOf(tmp[0]);
                name = tmp[1];
                parentCode = Integer.valueOf(tmp[2]);
                city = new City(code, name, parentCode);
                cities.add(city);
                mLine = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cities;
    }

    /**
     * 加载国家码
     */
    public static ArrayList<Country> loadCountries() {
        ArrayList<Country> countrys = new ArrayList<>();
        AssetManager am = ZHApplication.APP_CONTEXT.getAssets();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    am.open(PATH_COUNTRY), "UTF-8"));
            String[] tmp;
            String mLine = reader.readLine();
            while (mLine != null) {
                Country tag = new Country();
                tmp = mLine.split("\t");
                if (tmp != null && tmp.length >= 3) {
                    tag.code = tmp[0];
                    tag.name = tmp[1];
                    tag.showCode = tmp[2];
                    countrys.add(tag);
                }
                mLine = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return countrys;
    }

    /**
     * 公司发展状态
     */
    public ArrayList<CompanyState> getCompanyState() {
        ArrayList<CompanyState> states = new ArrayList<>();
        AssetManager am = ZHApplication.APP_CONTEXT.getAssets();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    am.open(PATH_COMPANY_STATE), "UTF-8"));
            String[] tmp;
            String mLine = reader.readLine();
            while (mLine != null) {
                CompanyState companyState = new CompanyState();
                tmp = mLine.split("\t");
                if (tmp != null && tmp.length >= 4) {
                    companyState.tagId = tmp[0];
                    companyState.tagName = tmp[1];
                    companyState.custom = tmp[2];
                    companyState.parentId = tmp[3];
                    states.add(companyState);
                }
                mLine = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return states;
    }

    /**
     * 加载公司类型
     */
    public ArrayList<CompanyType> getCompanyType() {
        ArrayList<CompanyType> types = new ArrayList<>();
        AssetManager am = ZHApplication.APP_CONTEXT.getAssets();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    am.open(PATH_COMPANY_TYPE), "UTF-8"));
            String[] tmp;
            String mLine = reader.readLine();
            while (mLine != null) {
                CompanyType companyType = new CompanyType();
                tmp = mLine.split("\t");
                if (tmp != null && tmp.length >= 4) {
                    companyType.tagId = tmp[0];
                    companyType.tagName = tmp[1];
                    companyType.custom = tmp[2];
                    companyType.parentId = tmp[3];
                    types.add(companyType);
                }
                mLine = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return types;
    }

    /**
     * 加载活动排序
     */
    public ArrayList<ZHDicItem> getEventOrder() {
        ArrayList<ZHDicItem> items = new ArrayList<>();
        AssetManager am = ZHApplication.APP_CONTEXT.getAssets();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    am.open(PATH_EVENT_ORDER), "UTF-8"));
            String[] tmp;
            String mLine = reader.readLine();
            while (mLine != null) {
                ZHDicItem dicItem = new ZHDicItem();
                tmp = mLine.split("\t");
                if (tmp != null && tmp.length >= 2) {
                    dicItem.key = tmp[0];
                    dicItem.name = tmp[1];
                    items.add(dicItem);
                }
                mLine = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return items;
    }

    /**
     * 获取活动标签
     */
    public List<String> getEventTags() {
        List<String> items = new ArrayList<>();
        AssetManager am = ZHApplication.APP_CONTEXT.getAssets();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    am.open(PATH_EVENT_TAG), "UTF-8"));
            String[] tmp;
            String mLine = reader.readLine();
            while (mLine != null) {
                tmp = mLine.split("\t");
                if (tmp != null && tmp.length == 1) {
                    items.add(tmp[0]);
                }
                mLine = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return items;
    }

    /**
     * 加载兴趣标签
     */
    public List<String> loadInterestTags() {
        List<String> tags = new ArrayList<String>();
        AssetManager am = ZHApplication.APP_CONTEXT.getAssets();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    am.open("interests.txt"), "UTF-8"));
            String value;
            String[] tmp;

            String mLine = reader.readLine();
            while (mLine != null) {
                tmp = mLine.split("\t");
                value = tmp[0];
                mLine = reader.readLine();

                tags.add(value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tags;
    }

    // 加载擅长领域标签
    public List<String> loadSpecialtieTags() {
        List<String> tags = new ArrayList<String>();
        AssetManager am = ZHApplication.APP_CONTEXT.getAssets();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    am.open("specialties.txt"), "UTF-8"));
            String value;
            String[] tmp;

            String mLine = reader.readLine();
            while (mLine != null) {
                tmp = mLine.split("\t");
                value = tmp[0];
                mLine = reader.readLine();

                tags.add(value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tags;
    }

    /**
     * 获取个人行业标签
     */
    public ArrayList<ZHDicItem> getIndustriesPersonal() {
        return loadFromAssets(PATH_TYPE_INDUSTRY_PERSONAL);
    }

    /**
     * 获取公司行业标签
     */
    public ArrayList<ZHDicItem> getIndustriesCompany() {
        return loadFromAssets(PATH_TYPE_INDUSTRY_COMPANY);
    }

    /**
     * 获取资源类型标签
     */
    public ArrayList<ZHDicItem> getSupplyCategory() {
        return loadFromAssets(PATH_SUPPLY_CATEGORY);
    }

    /**
     * 获取需求类型标签
     */
    public ArrayList<ZHDicItem> getDemandCategory() {
        return loadFromAssets(PATH_DEMAND_CATEGORY);
    }

    private ArrayList<ZHDicItem> loadFromAssets(String assetsPath) {
        ArrayList<ZHDicItem> tags = new ArrayList<ZHDicItem>();
        AssetManager am = ZHApplication.APP_CONTEXT.getAssets();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    am.open(assetsPath), "UTF-8"));
            String[] tmp;
            String mLine = reader.readLine();
            while (mLine != null) {
                ZHDicItem item;
                tmp = mLine.split("\t");
                if (tmp != null && tmp.length >= 2) {
                    item = new ZHDicItem(tmp[0], tmp[1]);
                    tags.add(item);
                }
                mLine = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tags;
    }
}

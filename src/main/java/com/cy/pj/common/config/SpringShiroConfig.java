package com.cy.pj.common.config;

import java.util.LinkedHashMap;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringShiroConfig {


	@Bean 
	public SessionManager sessionManager() { 
		DefaultWebSessionManager sManager=new DefaultWebSessionManager();
	sManager.setGlobalSessionTimeout(60*60*1000); return sManager; }

	@Bean 
	public RememberMeManager RememberMeManager() { CookieRememberMeManager
		crm=new CookieRememberMeManager(); SimpleCookie cookie=new
		SimpleCookie("rememberMe"); cookie.setMaxAge(60*60); crm.setCookie(cookie);
		return crm; }

	@Bean
	public CacheManager shiroCacheManager() {
		return new MemoryConstrainedCacheManager();
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor anewAuthorizationAttributeSourceAdvisor(
			SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}

	@Bean
	public SecurityManager securityManager(Realm realm,
			CacheManager cacheManager, RememberMeManager rememberMeManager, SessionManager sessionManager) {
		DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
		manager.setRealm(realm);
		manager.setCacheManager(cacheManager);
		manager.setRememberMeManager(rememberMeManager);
	    manager.setSessionManager(sessionManager);
		return manager;
	}

	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager magger) {
		ShiroFilterFactoryBean sf = new ShiroFilterFactoryBean();
		sf.setSecurityManager(magger);
		sf.setLoginUrl("/doLoginUI");
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		map.put("/bower_components/**", "anon");
		map.put("/build/**", "anon");
		map.put("/dist/**", "anon");
		map.put("/plugins/**", "anon");
		map.put("/user/doLogin", "anon");
		map.put("/doLogout", "logout");
		//map.put("/**","authc");
		map.put("/**", "user");
		sf.setFilterChainDefinitionMap(map);
		return sf;
	}
	/*
	 * @Bean public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() { return
	 * new LifecycleBeanPostProcessor(); }
	 * 
	 * @DependsOn("lifecycleBeanPostProcessor")
	 * 
	 * @Bean public DefaultAdvisorAutoProxyCreator
	 * newDefaultAdvisorAutoProxyCreator() { return new
	 * DefaultAdvisorAutoProxyCreator(); }
	 */
}

# TencentMapLib

在项目根目录的build.gradle添加这一句代码。
```
Add it in your root build.gradle at the end of repositories:
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
在app目录下的build.gradle添加依赖使用。
```
  dependencies {
	   implementation 'com.github.x24232022:TencentMapLib:v1.0'
  }
```

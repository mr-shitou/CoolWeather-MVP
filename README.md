# CoolWeather-MVP
《第一行代码》第二版CoolWeather的MVP版

## 为什么有这个

**个人练习MVP模式，所以选用这个可能每个人都熟悉的CoolWeather来玩玩** emjio :smile:

> 需要注意的地方

* 个人理解来改的这个程序，如果有疑问和错误还希望在Issue中提出来，什么感想
* 可能有些地方，代码比较简单，应该写在View层的东西，直接在Presenter中作了简单调用
* WeatherActivity中没有做View层和Presenter层的处理，懒了。嘿嘿

> 简单写下自己的感受和想法

* MVP模式比原版“罗嗦”，尤其是在小型并耦合度很高的APP里。所以，虽然写成了MVP模式，但是不推荐，还是原版好。
* MVP模式以及其他模式，推荐去看[Android Architecture](https://github.com/googlesamples/android-architecture)。最好能把程序跟着写一遍，再去好好琢磨，会有很大的收获。另外极力推荐看下[Android Architecture Components](https://developer.android.google.cn/topic/libraries/architecture/index.html)
* View层和Presenter层通过接口通信，具体讲，就是View需要逻辑操作时调用Presenter的接口；Presenter完成数据处理后调用View层的接口来更新界面。同时，最好用Fragment来充当View层，Activity用来控制View和Presenter。
* 至于接口的定义，简单讲就是有逻辑操作就需要Presenter接口，有UI操作就需要View接口。并且接口数不一定相同，千万不要被这个迷惑，曾经有个小伙伴就问过这个问题。

## 感谢

[Android Architecture](https://github.com/googlesamples/android-architecture)

[郭霖大神](https://github.com/guolindev)以及[CoolWeather](https://github.com/guolindev/coolweather)

如果有侵权或者不妥之处还希望指出，立刻修改。
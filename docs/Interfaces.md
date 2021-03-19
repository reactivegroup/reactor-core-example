onAssembly 是一个扩展机制，因为实例中并没有使用，所以可简单理解为将入参直接返回。接下来 new FluxArray<>(array) 做的事情也很简单，仅仅是把入参赋给成员变量。再接下来 filter(s -> s.length() > 3) 做的事情是把上一个 Flux，即 FluxArray，以及 s -> s.length() > 3 所表示的 Predicate 保存起来。

返回的值分为了 Fuseable 和非 Fuseable 两种。简单介绍一下，Fuseable 的意思就是可融合的。我理解就是 Flux 表示的是一个类似集合的概念，有一些集合类型可以将多个元素融合在一起，打包处理，而不必每个元素都一步步的处理，从而提高了效率。因为 FluxArray 中的数据一开始就都准备好了，因此可以打包处理，因此就是 Fuseable。

其中的 source 变量对应的是当前 Flux 的上一个。本例中，FluxMapFuseable 上一个是 FluxFilterFuseable。

new MapFuseableSubscriber<>(actual, mapper) 则是将订阅了 FluxMapFuseable 的 Subscriber 和映射器封装在一起，组成一个新的 Subscriber。然后再订阅 source，即 FluxArray。source 是在上一个阶段被保存下来的。

这里强调一下 Publisher 接口中的 subscribe 方法语义上有些奇特，它表示的不是订阅关系，而是被订阅关系。即 aPublisher.subscribe(aSubscriber) 表示的是 aPublisher 被 aSubscriber 订阅。

可以这样简单理解，对于中间过程的 Mono/Flux，subscribe 阶段是订阅上一个 Mono/Flux；而对于源 Mono/Flux，则是要执行 Subscriber.onSubscribe(Subscription s) 方法。

onSubscribe 阶段是表示订阅动作的方式，让各 Subscriber 知悉，准备开始处理数据。当最终的 Subscriber 做好处理数据的准备之后，它便会调用 Subscription 的 request 方法请求数据。
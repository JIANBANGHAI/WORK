重构代码：
将响应客户端的操作拆分出去，并且重用响应的代码
实现：
1：在com.webserver.http包下新建一个类：HttpResponse响应对象
使用这个类的每一个实例给客户端发送的一个响应内容
2：定义一个flush方法（内部再定义三个发送响应的细节方法）来启用发送响应的操作
3：将ClientHandler原本发送响应的代码移动到flush对应·的三个细节方法中。
4：将ClientHandle处理请求环节发送响应工作删除，改为通过设置HttpResponse完成响应
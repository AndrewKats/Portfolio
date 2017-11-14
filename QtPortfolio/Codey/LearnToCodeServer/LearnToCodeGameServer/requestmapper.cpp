#include "requestmapper.h"
#include "logincontroller.h"
#include "signupcontroller.h"
#include "savecontroller.h"
#include "statscontroller.h"

RequestMapper::RequestMapper(QObject* parent)
    : HttpRequestHandler(parent) {
    // empty
}

StaticFileController* RequestMapper::staticFileController=0;

void RequestMapper::service(HttpRequest& request, HttpResponse& response) {
    QByteArray path=request.getPath();
    qDebug("RequestMapper: path=%s",path.data());

    if (path=="/login") {
        LoginController().service(request, response);
    }
    else if (path=="/signup") {
        SignupController().service(request, response);
    }
    else if (path == "/save") {
        SaveController().service(request, response);
    }
    else if (path.startsWith("/files")) {

    }
    else if (path == "/") {
        StatsController().service(request, response);
        staticFileController->service(request,response);
    }
    else {
        staticFileController->service(request,response);
    }

    qDebug("RequestMapper: finished request");
}

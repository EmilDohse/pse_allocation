/*
 * package deadline;
 * 
 * import java.util.Optional;
 * 
 * import javax.inject.Inject;
 * 
 * import play.api.mvc.Handler; import play.core.j.JavaHandler; import
 * play.core.j.JavaHandlerComponents; import play.http.HandlerForRequest; import
 * play.http.HttpRequestHandler; import play.mvc.Http.RequestHeader; import
 * play.routing.Router; import scala.Option; import scala.compat.java8.*; import
 * play.mvc.Results; import play.mvc.EssentialAction; import
 * play.libs.streams.Accumulator;
 * 
 * /** Dieser HttpRequest Handler ist dafür da, um die Deadlines
 * (Registrierungsstart/Registrierungsende) zu implementieren. Dabei werden
 * verschiedene Routingtabellen verwendet, um während den verschhiedenen Phasen
 * verschiedene URLs zu blocken.
 * 
 * @author daniel
 *
 *//*
   * public class DeadlineHttpRequestHandler implements HttpRequestHandler {
   * 
   * private final standard.Routes standardRouter; private final
   * JavaHandlerComponents components; private final beforeRegistration.Routes
   * beforeRegistrationRouter; private final registration.Routes
   * registrationRouter; private final afterRegistration.Routes
   * afterRegistrationRouter;
   * 
   * @Inject public DeadlineHttpRequestHandler(standard.Routes standardRouter,
   * JavaHandlerComponents components, beforeRegistration.Routes
   * beforeRegistrationRouter, registration.Routes registrationRouter,
   * afterRegistration.Routes afterRegistrationRouter) { this.standardRouter =
   * standardRouter; this.components = components; this.beforeRegistrationRouter
   * = beforeRegistrationRouter; this.afterRegistrationRouter =
   * afterRegistrationRouter; this.registrationRouter = registrationRouter; }
   * 
   * @Override public HandlerForRequest handlerForRequest(RequestHeader request)
   * { Optional<Handler> optionalHandler = OptionConverters
   * .toJava(beforeRegistrationRouter .handlerFor(request._underlyingHeader()));
   * Handler handler; Option<Handler> scalaStandardOptionalHandler =
   * standardRouter .handlerFor(request._underlyingHeader()); Handler
   * standardHandler = OptionConverters .toJava(scalaStandardOptionalHandler)
   * .orElseGet(() -> EssentialAction .of(req ->
   * Accumulator.done(Results.notFound())));
   * 
   * handler = optionalHandler.orElse(standardHandler); if (handler instanceof
   * JavaHandler) { handler = ((JavaHandler)
   * handler).withComponents(components); } return new
   * HandlerForRequest(request, handler); }
   * 
   * private Optional<Handler> getHandlerAfterCorrectRouting( RequestHeader
   * request) { Optional<Handler> optionalHandler; switch
   * (StateStorage.getInstance().getCurrentState()) { case
   * BEFORE_REGISTRATION_PHASE: optionalHandler =
   * OptionConverters.toJava(beforeRegistrationRouter
   * .handlerFor(request._underlyingHeader())); break; case REGISTRATION_PHASE:
   * optionalHandler = OptionConverters.toJava(
   * registrationRouter.handlerFor(request._underlyingHeader())); break; case
   * AFTER_REGISTRATION_PHASE: optionalHandler =
   * OptionConverters.toJava(afterRegistrationRouter
   * .handlerFor(request._underlyingHeader())); break; default: optionalHandler
   * = OptionConverters.toJava(
   * standardRouter.handlerFor(request._underlyingHeader())); } return
   * optionalHandler; } }
   */
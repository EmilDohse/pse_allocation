package controllers;

import play.http.HttpErrorHandler;
import play.mvc.*;
import play.mvc.Http.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Singleton;

/**
 * ErrorHandler-Klasse welche nicht definierte Routen auf eine Fehler-Seite
 * routet.
 * 
 * @author daniel
 *
 */
@Singleton
public class ErrorHandler implements HttpErrorHandler {

    /**
     * Wird aufgerufen wenn auf der Client-Seite ein Fehler auftritt.
     */
    public CompletionStage<Result> onClientError(RequestHeader request,
            int statusCode, String message) {
        if (statusCode == 404) {
            return CompletableFuture.completedFuture(Results.status(statusCode,
                    "A client error occurred: Page not found."));
        }
        return CompletableFuture.completedFuture(Results.status(statusCode,
                "A client error occurred: " + message));
    }

    /**
     * Wird aufgerufen wenn auf der Server-Seite ein Fehler auftritt.
     */
    public CompletionStage<Result> onServerError(RequestHeader request,
            Throwable exception) {
        return CompletableFuture.completedFuture(Results.internalServerError(
                "A server error occurred: " + exception.getMessage()));
    }
}
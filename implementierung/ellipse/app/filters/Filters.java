package filters;

import javax.inject.Inject;

import org.pac4j.play.filters.SecurityFilter;

import deadline.DeadlineFilter;
import play.http.HttpFilters;
import play.mvc.EssentialFilter;

public class Filters implements HttpFilters {

    private final SecurityFilter securityFilter;
    private final DeadlineFilter deadlineFilter;

    @Inject
    public Filters(SecurityFilter securityFilter,
            DeadlineFilter deadlineFilter) {
        this.securityFilter = securityFilter;
        this.deadlineFilter = deadlineFilter;
    }

    @Override
    public EssentialFilter[] filters() {
        return new EssentialFilter[] { deadlineFilter,
                securityFilter.asJava() };
    }
}

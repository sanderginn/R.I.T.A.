package domainapp.modules.rita.dom.ride.mixins;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;

import domainapp.modules.rita.dom.car.Car;
import domainapp.modules.rita.dom.ride.Ride;
import domainapp.modules.rita.dom.ride.RideRepository;

@Mixin
public class Car_rides {

    private final Car car;

    public Car_rides(final Car car) {
        this.car = car;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_ASSOCIATION)
    public SortedSet<Ride> $$() {
        return new TreeSet<>(rideRepository.findByCar(car));
    }

    @Inject
    RideRepository rideRepository;
}

package com.example.power;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.PathVariable;

import java.util.List;

@Controller("/power")
public class PowerController {

    private final PowerService powerService;

    public PowerController(PowerService powerService) {
        this.powerService = powerService;
    }

    @Get
    public HttpResponse<List<Power>> getAll() {
        return HttpResponse.ok(powerService.getAll());
    }

    @Post
    public HttpResponse<Power> getOrCreatePower(@Body String powerName) {
        return HttpResponse.created(powerService.getOrCreate(powerName));
    }

    @Delete("/{id}")
    public HttpResponse<Void> delete(@PathVariable Integer id) {
        powerService.delete(id);
        return HttpResponse.ok();
    }

    @Delete("/delete-all")
    public HttpResponse<Void> deleteAll() {
        powerService.deleteAll();
        return HttpResponse.ok();
    }

}

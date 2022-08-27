package minicore.mvc;

import minicore.contracts.HttpContext;
import minicore.contracts.formaters.IFormatProvider;
import minicore.contracts.formaters.IInputFormatter;
import minicore.contracts.formaters.IOutputFormatter;
import minicore.contracts.mvc.MvcConfigurer;
import minicore.contracts.results.SupportedMediaTypes;
import minicore.mvc.formatters.JsonOutputFormatter;
import minicore.mvc.formatters.DefaultInputFormatter;
import org.eclipse.jetty.util.IO;

import java.util.ArrayList;
import java.util.List;

public class FormatProvider implements IFormatProvider {
    private final MvcConfigurer configurer;
    private final List<IInputFormatter> inputFormatters ;
    private final List<IOutputFormatter> outputFormatters;
    private  final SupportedMediaTypes inputMediaTypes= new SupportedMediaTypes();
    private  final SupportedMediaTypes outputMediaTypes= new SupportedMediaTypes();

    public FormatProvider(MvcConfigurer configurer) {
        this.configurer = configurer;
        inputFormatters = new ArrayList<>();
        outputFormatters = new ArrayList<>();
        createFormatters();
    }

    private void createFormatters() {
        this. configurer.getInputFormatters().forEach(x->{
            IInputFormatter ipf= HttpContext.services.resolve(x);
           inputMediaTypes.add(ipf.supportedMediaType());
            inputFormatters.add(ipf);
        });
        this. configurer.getOutputFormatters().forEach(x->{
            IOutputFormatter opf= HttpContext.services.resolve(x);
            outputMediaTypes.add(opf.supportedMediaType());
            outputFormatters.add(opf);
        });
    }

    @Override
    public boolean canSuportedInputMediaType(String mediaType) {
        return false;
    }

    @Override
    public boolean canSuportedOutpMediaType(String mediaType) {
        return false;
    }

    @Override
    public IInputFormatter getSuportedInputFormatter(HttpContext context) {

         return inputFormatters.stream().filter(x->x.canSupport(context)).findFirst().orElse(new DefaultInputFormatter());
    }

    @Override
    public IOutputFormatter getSupportedOutputFormatter(HttpContext context) {
        return outputFormatters.stream().filter(x->x.canSupport(context)).findFirst().orElse(new JsonOutputFormatter());
    }
}

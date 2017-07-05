package rogueone.quizfight.rest.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import rogueone.quizfight.QuizFightApplication;
import rogueone.quizfight.rest.EndpointInterface;
import rogueone.quizfight.rest.RetrofitHelper;

/**
 * Created by mdipirro on 19/05/17.
 *
 * The hierarchy instantiate the Template Method pattern. Given an <tt>apiService</tt> one can use the
 * <tt>call</tt> method just providing a <tt>Callback</tt> object. One must use <tt>call</tt>
 * from a concrete <tt>APICaller</tt> instance. The latter must implement the <tt>getActualMethod</tt>
 * method, providing the right <tt>EndpointInterface</tt> REST service.
 *
 * @author  Matteo Di Pirro
 * @see EndpointInterface
 * @see RetrofitHelper
 */

public abstract class APICaller<T> {
    protected EndpointInterface apiService;

    public APICaller() {
        apiService = RetrofitHelper.getAPIService();
    }

    public void call(@NonNull Callback<T> callback, QuizFightApplication application) {
        if (application.checkConnection(application.getApplicationContext())) {
            Call<T> api = getActualMethod();
            api.enqueue(callback);
        }
        else {
            Toast.makeText(application.getApplicationContext(),
                    "YOU ARE NOT CONNECTED",
                    Toast.LENGTH_LONG).show();
        }
    }

    protected abstract Call<T> getActualMethod();
}

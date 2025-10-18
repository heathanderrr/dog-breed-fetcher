package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) {
        final OkHttpClient build = client.newBuilder().build();
        final Request request = new Request.Builder()
                .url(String.format("https://dog.ceo/api/%s/list", breed))
                .build();
        try {
            final Response response = build.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());
            if (responseBody.getString("status").matches("success")) {
                final JSONArray subBreeds = responseBody.getJSONArray("message");
                final List<String> result = new ArrayList<>();
                for (int i = 0; i < subBreeds.length(); i++) {
                    result.add(subBreeds.getString(i));
                } return result;
            } else {
                throw new BreedNotFoundException(breed);
            }
        } catch (IOException breedNotFoundException) {
            throw new BreedNotFoundException(breed);
        }

    }
}
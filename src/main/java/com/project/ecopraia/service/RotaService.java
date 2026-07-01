package com.project.ecopraia.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.project.ecopraia.entity.enums.ModoDeslocamento;
import com.project.ecopraia.exception.ServicoExternoIndisponivelException;

@Service
public class RotaService {

    private static final String BASE_URL = "https://api.openrouteservice.org/v2/directions";

    private final RestClient restClient;
    private final String apiKey;

    public RotaService(@Value("${openrouteservice.api-key}") String apiKey) {
        this.apiKey = apiKey;
        this.restClient = RestClient.create();
    }

    public record RotaResultado(double distanciaMetros, double duracaoSegundos) {
    }

    @SuppressWarnings("unchecked")
    public RotaResultado calcularRota(double latOrigem, double lonOrigem,
            double latDestino, double lonDestino, ModoDeslocamento modo) {

        Map<String, Object> body = Map.of(
                "coordinates", List.of(
                        List.of(lonOrigem, latOrigem),
                        List.of(lonDestino, latDestino)));

        Map<String, Object> resposta;
        try {
            resposta = restClient.post()
                    .uri(BASE_URL + "/" + modo.getPerfilOrs())
                    .header("Authorization", apiKey)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .retrieve()
                    .body(Map.class);
        } catch (RestClientException ex) {
            throw new ServicoExternoIndisponivelException(
                    "Não foi possível calcular a rota no momento. Tente novamente mais tarde.", ex);
        }

        List<Map<String, Object>> routes = resposta == null
                ? null
                : (List<Map<String, Object>>) resposta.get("routes");

        if (routes == null || routes.isEmpty()) {
            throw new ServicoExternoIndisponivelException(
                    "Não foi possível calcular a rota para o trajeto informado.");
        }

        Map<String, Object> summary = (Map<String, Object>) routes.get(0).get("summary");
        double distancia = ((Number) summary.get("distance")).doubleValue();
        double duracao = ((Number) summary.get("duration")).doubleValue();

        return new RotaResultado(distancia, duracao);
    }

    public static String formatarDuracao(double segundos) {
        long totalMin = Math.round(segundos / 60.0);
        long horas = totalMin / 60;
        long min = totalMin % 60;
        if (horas > 0) {
            return horas + " h " + min + " min";
        }
        return min + " min";
    }
}

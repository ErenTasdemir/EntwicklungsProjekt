package com.github.entwicklungsprojekt.shop.service;

import com.github.entwicklungsprojekt.openstreetmap_location.service.OpenstreetmapConnectionService;
import com.github.entwicklungsprojekt.shop.persistence.Shop;
import com.github.entwicklungsprojekt.shop.persistence.ShopRepository;
import com.github.entwicklungsprojekt.shop.projection.ShopProjectionWithPicture;
import com.github.entwicklungsprojekt.shop.projection.ShopProjectionWithoutPicture;
import com.github.entwicklungsprojekt.shop.search.HibernateSearchService;
import lombok.AllArgsConstructor;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@AllArgsConstructor
@Service
public class ShopService {

    private final ShopRepository shopRepository;

    private final HibernateSearchService shopSearchService;

    private final OpenstreetmapConnectionService openstreetmapConnectionService;

    private final ProjectionFactory projectionFactory;


    public List<Shop> getAllAvailibleShops() {
        return shopRepository.findAll();
    }

    public List<Shop> searchShops(String query) {
        if (query.isEmpty()) {
            return shopRepository.findAll();
        }
        return shopSearchService.searchShops(query);
    }

    public Shop getShopWithPictureById(Long id) {
        Shop shop = shopRepository.getOne(id);
        shop.setShopImage(decompressBytes(shop.getShopImage()));
        return shop;
    }

    public Shop addShop(String shopName, String shopLocation, String shopType) {
        Shop shop = new Shop(shopName, shopLocation, shopType);
        shopRepository.save(shop);
        openstreetmapConnectionService.setLatitudeAndLongitudeForGivenShop(shop);

        return shop;
    }

    public Shop editShop(Long shopId, String newName, String newLocation, String newType) {
        Shop shopToEdit = shopRepository.getOne(shopId);
        shopToEdit.setShopName(newName);
        shopToEdit.setShopType(newType);
        shopToEdit.setShopLocation(newLocation);
        shopRepository.save(shopToEdit);
        openstreetmapConnectionService.setLatitudeAndLongitudeForGivenShop(shopToEdit);

        return shopToEdit;
    }

    public Shop deleteShop(Long shopId) {
        Shop shop = shopRepository.getOne(shopId);
        shopRepository.delete(shop);
        return shop;
    }

    public Shop saveImageToShop(Long shopId, MultipartFile image) throws IOException {
        Shop shop = shopRepository.getOne(shopId);
        shop.setShopImage(compressBytes(image.getBytes()));
        shopRepository.save(shop);
        return shop;
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException ignored) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException ignored) {
        }
        return outputStream.toByteArray();
    }


}
